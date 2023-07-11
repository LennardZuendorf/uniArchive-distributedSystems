export default class ProductService {

    getProductsSmall() {
        const response = [
            {id: "1000",code: "f230fh0g3",name: "Bamboo Watch",description: "Product Description",image: "bamboo-watch.jpg",price: 65,category: "Accessories",quantity: 24,inventoryStatus: "INSTOCK",rating: 5},
            {id: "1001",code: "nvklal433",name: "Black Watch",description: "Product Description",image: "black-watch.jpg",price: 72,category: "Accessories",quantity: 61,inventoryStatus: "INSTOCK",rating: 4},
        ]
        return response
//        return fetch('demo/data/products-small.json').then(res => res.json()).then(d => d.data);
    }

    getProducts() {
        return fetch('demo/data/products.json').then(res => res.json()).then(d => d.data);
    }

    getProductsWithOrdersSmall() {
        return fetch('demo/data/products-orders-small.json').then(res => res.json()).then(d => d.data);
    }
}