package hello.itemservice.domain.item;


import lombok.Getter;
import lombok.Setter;

//같이 쓰지말구 getter setter만 사용하자 @Data
@Getter
@Setter
public class Item {
    private Long id;
    private String itemName;
    private Integer price;
    private Integer quantity;

    public Item(){

    }

    public Item(String itemName, Integer price, Integer quantity) {
        this.itemName = itemName;
        this.price = price;
        this.quantity = quantity;
    }

}
