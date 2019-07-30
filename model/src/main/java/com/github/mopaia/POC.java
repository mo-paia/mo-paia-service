package com.github.mopaia;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.mopaia.model.Customer;
import com.github.mopaia.model.Payment;
import com.github.mopaia.model.Product;
import com.github.mopaia.model.Purchase;
import com.github.mopaia.model.PurchaseItem;
import com.github.mopaia.model.Sale;
import com.github.mopaia.model.SaleItem;
import com.github.mopaia.model.Stock;
import com.github.mopaia.model.StockItem;
import com.github.mopaia.utils.CustomObjectMapper;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.UUID;

public class POC {

    public static void main(String[] args) throws JsonProcessingException {

        /*
            - Product é uma forma de controlarmos todos os produtos que temos na plataforma, seja para compra ou para venda

            {
                 "id": "90fc9923-b0e8-4939-9ca2-cbb1034e254c",
                 "name": "Maço Palheiro Paulistinha Tradicional 20 unidades"
             }
         */
        Product product = new Product();
        product.setId(UUID.randomUUID());
        product.setName("Maço Palheiro Paulistinha Tradicional 20 unidades");


        /*
            - Purchase são nossas compras com fornecedores
            - PurchaseItem são os items (products) comprados no fornecedor, com o preço original e quantidade

            {
                "id": "a49efcf7-4c6a-451c-9b5c-cb99d42bf82a",
                "provider": "Tabacaria Sales Oliveira",
                "providerOrderId": "6960",
                "providerOrderUrl": "https://tabacariasalesoliveira.com.br/minha-conta/view-order/6960",
                "purchaseItems": [{
                    "id": "88c801a1-0b2b-4092-b654-d2b469541656",
                    "product": {
                        "id": "90fc9923-b0e8-4939-9ca2-cbb1034e254c",
                        "name": "Maço Palheiro Paulistinha Tradicional 20 unidades"
                    },
                    "quantity": 10,
                    "purchasePrice": 10
                }]
            }
         */
        Purchase purchase = new Purchase();
        purchase.setId(UUID.randomUUID());
        purchase.setProvider("Tabacaria Sales Oliveira");
        purchase.setProviderOrderId("6960");
        purchase.setProviderOrderUrl("https://tabacariasalesoliveira.com.br/minha-conta/view-order/6960");

        PurchaseItem purchaseItem = new PurchaseItem();
        purchaseItem.setId(UUID.randomUUID());
        purchaseItem.setProduct(product);
        purchaseItem.setPurchase(purchase);
        purchaseItem.setPurchasePrice(new BigDecimal(10));
        purchaseItem.setQuantity(10);
        purchase.setPurchaseItems(Collections.singletonList(purchaseItem));


        /*
            - Stock é uma entidade virtual que usamos para disponibilizar os produtos (product) em locais fisicos
                para nossos clientes, por exemplo no escritório do iFood na rua Jasmin
            - StockItem são os items disponibilizados para venda, com a referencia do item comprado (purchase>purchaseItem),
                com valor de venda e quantidade à disponibilizar

            {
                "id": "b347d2b2-7785-4de4-8a90-ab181379c6f6",
                "name": "iFood Campinas - Jasmin",
                "stockItems": [{
                    "id": "740c2de0-2f99-4b05-a4f5-9ea0fb05fce8",
                    "purchaseItem": {
                        "id": "88c801a1-0b2b-4092-b654-d2b469541656",
                        "product": {
                            "id": "90fc9923-b0e8-4939-9ca2-cbb1034e254c",
                            "name": "Maço Palheiro Paulistinha Tradicional 20 unidades"
                        },
                        "quantity": 10,
                        "purchasePrice": 10
                    },
                    "quantity": 10,
                    "salePrice": 19
                }]
            }
         */

        Stock stock = new Stock();
        stock.setId(UUID.randomUUID());
        stock.setName("iFood Campinas - Jasmin");

        StockItem stockItem = new StockItem();
        stockItem.setId(UUID.randomUUID());
        stockItem.setPurchaseItem(purchaseItem);
        stockItem.setSalePrice(new BigDecimal(19));
        stockItem.setQuantity(10);
        stock.setStockItems(Collections.singletonList(stockItem));


        /*
            - Sale representa uma venda de um produto disponibilizado (stock>stockItem) para um cliente (customer) que
                já pagou (payment) pelos produtos.
            - SaleItem representa um produto disponibilizado (stock>stockItem) que foi foi vendido com a quantidade
            - Customer representa o cliente
            - Payment representa o pagamento dessa venda, efetuado pelo cliente

            {
                "id": "f73d9d29-458a-4c29-8116-1170fb96a59c",
                "customer": {
                    "id": "b7199423-7ff5-48d7-8baf-aafe7020639a",
                    "name": "Conilheira",
                    "email": "conilheira@dosandes.com.br",
                    "phone": "+5519999999999"
                },
                "payment": {
                    "id": "cc674dc8-541e-4844-8ac9-827aa2bc940a",
                    "method": "TED",
                    "origin": "CUSTOMER",
                    "destination": "NUCONTA",
                    "totalValue": 19
                },
                "items": [{
                    "id": "a5d66b3a-36e9-4f42-920c-f3fa505583a5",
                    "stockItem": {
                        "id": "740c2de0-2f99-4b05-a4f5-9ea0fb05fce8",
                        "purchaseItem": {
                            "id": "88c801a1-0b2b-4092-b654-d2b469541656",
                            "product": {
                                "id": "90fc9923-b0e8-4939-9ca2-cbb1034e254c",
                                "name": "Maço Palheiro Paulistinha Tradicional 20 unidades"
                            },
                            "quantity": 10,
                            "purchasePrice": 10
                        },
                        "quantity": 10,
                        "salePrice": 19
                    },
                    "quantity": 1
                }]
            }

         */
        Sale sale = new Sale();
        sale.setId(UUID.randomUUID());

        SaleItem saleItem = new SaleItem();
        saleItem.setId(UUID.randomUUID());
        saleItem.setStockItem(stockItem);
        saleItem.setQuantity(1);
        saleItem.setSale(sale);
        sale.setItems(Collections.singletonList(saleItem));

        Customer customer = new Customer();
        customer.setId(UUID.randomUUID());
        customer.setName("Conilheira");
        customer.setEmail("conilheira@dosandes.com.br");
        customer.setPhone("+5519999999999");
        sale.setCustomer(customer);

        Payment payment = new Payment();
        payment.setId(UUID.randomUUID());
        payment.setMethod(Payment.Method.TED);
        payment.setOrigin(Payment.Origin.CUSTOMER);
        payment.setDestination(Payment.Destination.NUCONTA);
        payment.setTotalValue(new BigDecimal(19));
        sale.setPayment(payment);



        // Json print
        System.out.println(CustomObjectMapper.getInstance().writeValueAsString(product));
        System.out.println(CustomObjectMapper.getInstance().writeValueAsString(purchase));
        System.out.println(CustomObjectMapper.getInstance().writeValueAsString(purchaseItem));
        System.out.println(CustomObjectMapper.getInstance().writeValueAsString(stock));
        System.out.println(CustomObjectMapper.getInstance().writeValueAsString(stockItem));
        System.out.println(CustomObjectMapper.getInstance().writeValueAsString(sale));
        System.out.println(CustomObjectMapper.getInstance().writeValueAsString(saleItem));
        System.out.println(CustomObjectMapper.getInstance().writeValueAsString(customer));
        System.out.println(CustomObjectMapper.getInstance().writeValueAsString(payment));

    }
}
