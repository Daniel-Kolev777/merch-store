package com.merchstore.services;

import com.merchstore.models.Order;
import com.merchstore.models.OrderItem;
import com.merchstore.models.ProductImage;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

@Service
public class EmailServiceImpl implements EmailService {

    private static final BigDecimal EUR_TO_BGN =
            new BigDecimal("1.95583");

    private static final String SALTY_EAST_LOGO_CID =
            "saltyEastLogo";

    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;

    @Value("${app.mail.admin}")
    private String adminEmail;

    @Value("${app.mail.from}")
    private String fromEmail;

    @Value("${app.mail.from-name}")
    private String fromName;

    public EmailServiceImpl(
            JavaMailSender mailSender,
            SpringTemplateEngine templateEngine
    ) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
    }

    @Override
    public void sendNewOrderEmail(Order order) {

        Context context = new Context();

        context.setVariable(
                "order",
                order
        );

        context.setVariable(
                "subtotalFormatted",
                formatPrice(order.getSubtotal())
        );

        context.setVariable(
                "deliveryPriceFormatted",
                formatPrice(order.getDeliveryPrice())
        );

        context.setVariable(
                "totalPriceFormatted",
                formatPrice(order.getTotalPrice())
        );

        Map<Long, String> itemPrices =
                new HashMap<>();

        Map<Long, String> itemTotalPrices =
                new HashMap<>();

        for (OrderItem item : order.getItems()) {

            if (item.getId() != null) {

                itemPrices.put(
                        item.getId(),
                        formatPrice(item.getPrice())
                );

                BigDecimal itemTotal =
                        item.getPrice().multiply(
                                BigDecimal.valueOf(
                                        item.getQuantity()
                                )
                        );

                itemTotalPrices.put(
                        item.getId(),
                        formatPrice(itemTotal)
                );
            }
        }

        context.setVariable(
                "itemPrices",
                itemPrices
        );

        context.setVariable(
                "itemTotalPrices",
                itemTotalPrices
        );

        Map<Long, String> imageCids =
                new HashMap<>();

        for (OrderItem item : order.getItems()) {

            if (item.getProduct() == null ||
                    item.getProduct().getImages() == null) {

                continue;
            }

            for (ProductImage image :
                    item.getProduct().getImages()) {

                if (image.getId() == null ||
                        image.getImageURL() == null) {

                    continue;
                }

                String cid =
                        "product-image-" + image.getId();

                imageCids.put(
                        image.getId(),
                        cid
                );
            }
        }

        context.setVariable(
                "imageCids",
                imageCids
        );

        String htmlContent =
                templateEngine.process(
                        "new-order",
                        context
                );

        try {

            MimeMessage message =
                    mailSender.createMimeMessage();

            MimeMessageHelper helper =
                    new MimeMessageHelper(
                            message,
                            true,
                            "UTF-8"
                    );

            helper.setFrom(
                    fromEmail,
                    fromName
            );

            helper.setTo(
                    adminEmail
            );

            helper.setSubject(
                    "New Order #" +
                            order.getId()
            );

            helper.setText(
                    htmlContent,
                    true
            );

            ClassPathResource logoResource =
                    new ClassPathResource(
                            "static/assets/images/salty_east_logo.jpg"
                    );

            if (logoResource.exists()) {

                helper.addInline(
                        SALTY_EAST_LOGO_CID,
                        logoResource,
                        "image/jpeg"
                );
            }

            for (OrderItem item :
                    order.getItems()) {

                if (item.getProduct() == null ||
                        item.getProduct().getImages() == null) {

                    continue;
                }

                for (ProductImage image :
                        item.getProduct().getImages()) {

                    if (image.getId() == null ||
                            image.getImageURL() == null) {

                        continue;
                    }

                    String cid =
                            imageCids.get(
                                    image.getId()
                            );

                    if (cid == null) {
                        continue;
                    }

                    Path imagePath =
                            Paths.get(
                                    image.getImageURL()
                                            .substring(1)
                            );

                    if (!Files.exists(imagePath)) {
                        continue;
                    }

                    FileSystemResource resource =
                            new FileSystemResource(
                                    imagePath.toFile()
                            );

                    String contentType =
                            Files.probeContentType(
                                    imagePath
                            );

                    if (contentType == null) {

                        contentType =
                                "image/jpeg";
                    }

                    helper.addInline(
                            cid,
                            resource,
                            contentType
                    );
                }
            }

            mailSender.send(
                    message
            );

        } catch (Exception e) {

            throw new RuntimeException(
                    "Failed to send order email!",
                    e
            );
        }
    }

    @Override
    public void sendOrderCreatedEmail(Order order) {

        Context context =
                new Context();

        context.setVariable(
                "order",
                order
        );

        context.setVariable(
                "subtotalFormatted",
                formatPrice(
                        order.getSubtotal()
                )
        );

        context.setVariable(
                "deliveryPriceFormatted",
                formatPrice(
                        order.getDeliveryPrice()
                )
        );

        context.setVariable(
                "totalPriceFormatted",
                formatPrice(
                        order.getTotalPrice()
                )
        );

        Map<Long, String> itemPrices =
                new HashMap<>();

        for (OrderItem item :
                order.getItems()) {

            if (item.getId() != null) {

                itemPrices.put(
                        item.getId(),
                        formatPrice(
                                item.getPrice()
                        )
                );
            }
        }

        context.setVariable(
                "itemPrices",
                itemPrices
        );

        Map<Long, String> imageCids =
                new HashMap<>();

        for (OrderItem item :
                order.getItems()) {

            if (item.getProduct() == null ||
                    item.getProduct().getImages() == null) {

                continue;
            }

            for (ProductImage image :
                    item.getProduct().getImages()) {

                if (image.getId() == null ||
                        image.getImageURL() == null) {

                    continue;
                }

                String cid =
                        "product-image-" +
                                image.getId();

                imageCids.put(
                        image.getId(),
                        cid
                );
            }
        }

        context.setVariable(
                "imageCids",
                imageCids
        );

        String htmlContent =
                templateEngine.process(
                        "order-created",
                        context
                );

        try {

            MimeMessage message =
                    mailSender.createMimeMessage();

            MimeMessageHelper helper =
                    new MimeMessageHelper(
                            message,
                            true,
                            "UTF-8"
                    );

            helper.setFrom(
                    fromEmail,
                    fromName
            );

            helper.setTo(
                    order.getCustomerEmail()
            );

            helper.setSubject(
                    "Поръчка #" +
                            order.getId() +
                            " е приета | Salty East"
            );

            helper.setText(
                    htmlContent,
                    true
            );


            // SALTY EAST LOGO

            ClassPathResource logoResource =
                    new ClassPathResource(
                            "static/assets/images/salty_east_logo.jpg"
                    );

            if (logoResource.exists()) {

                helper.addInline(
                        SALTY_EAST_LOGO_CID,
                        logoResource,
                        "image/jpeg"
                );
            }


            // PRODUCT IMAGES

            for (OrderItem item :
                    order.getItems()) {

                if (item.getProduct() == null ||
                        item.getProduct().getImages() == null) {

                    continue;
                }

                for (ProductImage image :
                        item.getProduct().getImages()) {

                    if (image.getId() == null ||
                            image.getImageURL() == null) {

                        continue;
                    }

                    String cid =
                            imageCids.get(
                                    image.getId()
                            );

                    if (cid == null) {
                        continue;
                    }

                    Path imagePath =
                            Paths.get(
                                    image.getImageURL()
                                            .substring(1)
                            );

                    if (!Files.exists(imagePath)) {
                        continue;
                    }

                    FileSystemResource resource =
                            new FileSystemResource(
                                    imagePath.toFile()
                            );

                    String contentType =
                            Files.probeContentType(
                                    imagePath
                            );

                    if (contentType == null) {

                        contentType =
                                "image/jpeg";
                    }

                    helper.addInline(
                            cid,
                            resource,
                            contentType
                    );
                }
            }

            mailSender.send(
                    message
            );

        } catch (Exception e) {

            throw new RuntimeException(
                    "Failed to send order created email!",
                    e
            );
        }
    }

    @Override
    public void sendOrderShippedEmail(Order order) {

        Context context =
                new Context();

        context.setVariable(
                "order",
                order
        );

        context.setVariable(
                "totalPriceFormatted",
                formatPrice(
                        order.getTotalPrice()
                )
        );

        String htmlContent =
                templateEngine.process(
                        "order-shipped",
                        context
                );

        try {

            MimeMessage message =
                    mailSender.createMimeMessage();

            MimeMessageHelper helper =
                    new MimeMessageHelper(
                            message,
                            true,
                            "UTF-8"
                    );

            helper.setFrom(
                    fromEmail,
                    fromName
            );

            helper.setTo(
                    order.getCustomerEmail()
            );

            helper.setSubject(
                    "Поръчка #" +
                            order.getId() +
                            " е изпратена | Salty East"
            );

            helper.setText(
                    htmlContent,
                    true
            );


            ClassPathResource logoResource =
                    new ClassPathResource(
                            "static/assets/images/salty_east_logo.jpg"
                    );

            if (logoResource.exists()) {

                helper.addInline(
                        SALTY_EAST_LOGO_CID,
                        logoResource,
                        "image/jpeg"
                );
            }


            mailSender.send(
                    message
            );

        } catch (Exception e) {

            throw new RuntimeException(
                    "Failed to send shipped order email!",
                    e
            );
        }
    }

    private String formatPrice(
            BigDecimal euroPrice
    ) {

        if (euroPrice == null) {

            return "0.00 € / 0.00 лв.";
        }

        BigDecimal euro =
                euroPrice.setScale(
                        2,
                        RoundingMode.HALF_UP
                );

        BigDecimal bgn =
                euroPrice
                        .multiply(EUR_TO_BGN)
                        .setScale(
                                2,
                                RoundingMode.HALF_UP
                        );

        return euro +
                " € / " +
                bgn +
                " лв.";
    }
}