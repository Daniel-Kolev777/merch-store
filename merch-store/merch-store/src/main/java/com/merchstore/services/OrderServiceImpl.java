package com.merchstore.services;

import com.merchstore.dtos.econt.EcontDeliveryPriceRequestDto;
import com.merchstore.dtos.econt.EcontOfficeDto;
import com.merchstore.dtos.order.OrderCreateDto;
import com.merchstore.dtos.order.OrderOutDto;
import com.merchstore.exceptions.AuthorizationException;
import com.merchstore.exceptions.BadRequestException;
import com.merchstore.exceptions.EntityNotFoundException;
import com.merchstore.helpers.OrderMapper;
import com.merchstore.helpers.RoleValidator;
import com.merchstore.models.Cart;
import com.merchstore.models.CartItem;
import com.merchstore.models.Order;
import com.merchstore.models.OrderItem;
import com.merchstore.models.Product;
import com.merchstore.models.User;
import com.merchstore.models.enums.DeliveryMethod;
import com.merchstore.models.enums.OrderStatus;
import com.merchstore.repositories.CartRepository;
import com.merchstore.repositories.OrderRepository;
import com.merchstore.repositories.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    private static final BigDecimal ECONT_ADDRESS_PRICE =
            new BigDecimal("6.58");

    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final OrderMapper orderMapper;
    private final EmailService emailService;
    private final EcontService econtService;
    private final EcontApiClient econtApiClient;

    public OrderServiceImpl(
            OrderRepository orderRepository,
            CartRepository cartRepository,
            ProductRepository productRepository,
            OrderMapper orderMapper,
            EmailService emailService,
            EcontService econtService,
            EcontApiClient econtApiClient) {

        this.orderRepository = orderRepository;
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
        this.orderMapper = orderMapper;
        this.emailService = emailService;
        this.econtService = econtService;
        this.econtApiClient = econtApiClient;
    }

    @Override
    @Transactional
    public OrderOutDto createOrder(
            User currentUser,
            String cartToken,
            OrderCreateDto orderCreateDto) {

        Cart cart;

        if (currentUser != null) {

            cart = cartRepository
                    .findByUserId(currentUser.getId())
                    .orElseThrow(() ->
                            new BadRequestException(
                                    "Cart not found!"
                            )
                    );

        } else {

            cart = cartRepository
                    .findByCartToken(cartToken)
                    .orElseThrow(() ->
                            new BadRequestException(
                                    "Cart not found!"
                            )
                    );
        }

        if (cart.getItems().isEmpty()) {

            throw new BadRequestException(
                    "Cart is empty!"
            );
        }

        Order order = getOrder(
                currentUser,
                cartToken,
                orderCreateDto
        );

        BigDecimal subtotal = BigDecimal.ZERO;
        BigDecimal totalWeight = BigDecimal.ZERO;

        for (CartItem cartItem : cart.getItems()) {

            Product product =
                    productRepository.findById(
                            cartItem.getProduct().getId()
                    ).orElseThrow(() ->
                            new EntityNotFoundException(
                                    "Product",
                                    "id",
                                    String.valueOf(
                                            cartItem.getProduct().getId()
                                    )
                            )
                    );

            validateProductForOrder(
                    product,
                    cartItem
            );

            OrderItem orderItem =
                    new OrderItem();

            orderItem.setProduct(
                    product
            );

            orderItem.setQuantity(
                    cartItem.getQuantity()
            );

            orderItem.setPrice(
                    product.getPrice()
            );

            orderItem.setSize(
                    cartItem.getSize()
            );

            orderItem.setOrder(
                    order
            );

            order.getItems().add(
                    orderItem
            );

            BigDecimal quantity =
                    BigDecimal.valueOf(
                            cartItem.getQuantity()
                    );

            BigDecimal itemTotal =
                    product.getPrice()
                            .multiply(quantity);

            subtotal =
                    subtotal.add(
                            itemTotal
                    );

            if (product.getWeight() == null) {

                throw new BadRequestException(
                        "Product weight is missing for product: "
                                + product.getName()
                );
            }

            BigDecimal itemWeight =
                    product.getWeight()
                            .multiply(quantity);

            totalWeight =
                    totalWeight.add(
                            itemWeight
                    );

            int newQuantity =
                    product.getQuantity()
                            - cartItem.getQuantity();

            product.setQuantity(
                    newQuantity
            );

            productRepository.save(
                    product
            );
        }

        if (totalWeight.compareTo(
                BigDecimal.ZERO
        ) <= 0) {

            throw new BadRequestException(
                    "Order weight must be greater than 0!"
            );
        }

        BigDecimal deliveryPrice =
                calculateDeliveryPrice(
                        order,
                        totalWeight
                );

        BigDecimal totalPrice =
                subtotal.add(
                        deliveryPrice
                );

        order.setSubtotal(
                subtotal
        );

        order.setDeliveryPrice(
                deliveryPrice
        );

        order.setTotalPrice(
                totalPrice
        );

        Order savedOrder =
                orderRepository.save(
                        order
                );

        emailService.sendNewOrderEmail(
                savedOrder
        );

        emailService.sendOrderCreatedEmail(
                savedOrder
        );

        cart.getItems().clear();

        cartRepository.save(
                cart
        );

        return orderMapper.fromOrderToOutDto(
                savedOrder
        );
    }

    @Override
    public OrderOutDto getById(
            User currentUser,
            String cartToken,
            Long orderId) {

        Order order =
                orderRepository.findById(
                        orderId
                ).orElseThrow(() ->
                        new EntityNotFoundException(
                                "Order",
                                "id",
                                String.valueOf(orderId)
                        )
                );

        if (currentUser != null) {

            if (RoleValidator.isAdmin(
                    currentUser
            )) {

                return orderMapper
                        .fromOrderToOutDto(
                                order
                        );
            }

            if (order.getUser() == null
                    || !order.getUser()
                    .getId()
                    .equals(
                            currentUser.getId()
                    )) {

                throw new AuthorizationException(
                        "You do not have access to this order!"
                );
            }

        } else {

            if (cartToken == null
                    || !cartToken.equals(
                    order.getGuestToken()
            )) {

                throw new AuthorizationException(
                        "You do not have access to this order!"
                );
            }
        }

        return orderMapper.fromOrderToOutDto(
                order
        );
    }

    @Override
    public List<OrderOutDto> getAll(
            User currentUser,
            String cartToken) {

        List<Order> orders;

        if (currentUser != null) {

            if (RoleValidator.isAdmin(
                    currentUser
            )) {

                orders =
                        orderRepository.findAll();

            } else {

                orders =
                        orderRepository.findByUserId(
                                currentUser.getId()
                        );
            }

        } else {

            if (cartToken == null) {

                return List.of();
            }

            orders =
                    orderRepository.findByGuestToken(
                            cartToken
                    );
        }

        return orders.stream()
                .map(
                        orderMapper::fromOrderToOutDto
                )
                .toList();
    }

    @Override
    public OrderOutDto shipOrder(
            User currentUser,
            Long orderId) {

        if (currentUser == null
                || !RoleValidator.isAdmin(
                currentUser
        )) {

            throw new AuthorizationException(
                    "Only admin can ship orders!"
            );
        }

        Order order =
                orderRepository.findById(
                        orderId
                ).orElseThrow(() ->
                        new EntityNotFoundException(
                                "Order",
                                "id",
                                String.valueOf(orderId)
                        )
                );

        if (order.getStatus()
                == OrderStatus.SHIPPED) {

            throw new BadRequestException(
                    "Order is already shipped!"
            );
        }

        order.setStatus(
                OrderStatus.SHIPPED
        );

        Order savedOrder =
                orderRepository.save(
                        order
                );

        emailService.sendOrderShippedEmail(
                savedOrder
        );

        return orderMapper.fromOrderToOutDto(
                savedOrder
        );
    }

    private void validateProductForOrder(
            Product product,
            CartItem cartItem) {

        if (!product.isActive()) {

            throw new BadRequestException(
                    "Product is no longer available: "
                            + product.getName()
            );
        }

        if (product.getQuantity() == null
                || product.getQuantity() <= 0) {

            throw new BadRequestException(
                    "Product is out of stock: "
                            + product.getName()
            );
        }

        if (cartItem.getQuantity()
                > product.getQuantity()) {

            throw new BadRequestException(
                    "Not enough product quantity in stock for "
                            + product.getName()
                            + "! Available quantity: "
                            + product.getQuantity()
            );
        }

        if (product.isHasSizes()) {

            if (cartItem.getSize() == null) {

                throw new BadRequestException(
                        "Size is required for product: "
                                + product.getName()
                );
            }

            if (product.getAvailableSizes() == null
                    || !product.getAvailableSizes()
                    .contains(
                            cartItem.getSize()
                    )) {

                throw new BadRequestException(
                        "Selected size is no longer available for product: "
                                + product.getName()
                );
            }

        } else {

            if (cartItem.getSize() != null) {

                throw new BadRequestException(
                        "Product does not use sizes: "
                                + product.getName()
                );
            }
        }
    }

    private Order getOrder(
            User currentUser,
            String cartToken,
            OrderCreateDto orderCreateDto) {

        if (orderCreateDto.getPaymentMethod()
                == null) {

            throw new BadRequestException(
                    "Payment method is required!"
            );
        }

        if (orderCreateDto.getDeliveryMethod()
                == null) {

            throw new BadRequestException(
                    "Delivery method is required!"
            );
        }

        Order order =
                new Order();

        order.setCustomerName(
                orderCreateDto.getCustomerName()
        );

        order.setCustomerEmail(
                orderCreateDto.getCustomerEmail()
        );

        order.setCustomerPhone(
                orderCreateDto.getCustomerPhone()
        );

        order.setPaymentMethod(
                orderCreateDto.getPaymentMethod()
        );

        order.setDeliveryMethod(
                orderCreateDto.getDeliveryMethod()
        );

        order.setUser(
                currentUser
        );

        if (currentUser == null) {

            order.setGuestToken(
                    cartToken
            );
        }

        if (orderCreateDto.getDeliveryMethod()
                == DeliveryMethod.ECONT_OFFICE) {

            if (orderCreateDto.getEcontOfficeCode()
                    == null
                    || orderCreateDto
                    .getEcontOfficeCode()
                    .isBlank()) {

                throw new BadRequestException(
                        "Econt office code is required!"
                );
            }

            EcontOfficeDto office =
                    econtService.getOfficeByCode(
                            orderCreateDto
                                    .getEcontOfficeCode()
                    );

            if (office == null) {

                throw new BadRequestException(
                        "Invalid Econt office!"
                );
            }

            order.setEcontOfficeCode(
                    office.getCode()
            );

            order.setEcontOfficeName(
                    office.getName()
            );

            order.setCity(
                    office.getCity()
            );

            order.setAddress(
                    office.getAddress()
            );

        } else if (
                orderCreateDto.getDeliveryMethod()
                        == DeliveryMethod.ECONT_ADDRESS) {

            if (orderCreateDto.getCity()
                    == null
                    || orderCreateDto
                    .getCity()
                    .isBlank()) {

                throw new BadRequestException(
                        "City is required!"
                );
            }

            if (orderCreateDto.getAddress()
                    == null
                    || orderCreateDto
                    .getAddress()
                    .isBlank()) {

                throw new BadRequestException(
                        "Address is required!"
                );
            }

            order.setCity(
                    orderCreateDto.getCity()
            );

            order.setAddress(
                    orderCreateDto.getAddress()
            );
        }

        return order;
    }

    private BigDecimal calculateDeliveryPrice(
            Order order,
            BigDecimal totalWeight) {

        if (order.getDeliveryMethod()
                == DeliveryMethod.ECONT_OFFICE) {

            EcontDeliveryPriceRequestDto requestDto =
                    new EcontDeliveryPriceRequestDto(
                            order.getCustomerName(),
                            order.getCustomerPhone(),
                            order.getEcontOfficeCode(),
                            totalWeight
                    );

            return econtApiClient
                    .calculateOfficeDeliveryPrice(
                            requestDto
                    );
        }

        if (order.getDeliveryMethod()
                == DeliveryMethod.ECONT_ADDRESS) {

            return ECONT_ADDRESS_PRICE;
        }

        throw new BadRequestException(
                "Unsupported delivery method!"
        );
    }
}