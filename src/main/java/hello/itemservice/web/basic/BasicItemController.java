package hello.itemservice.web.basic;

import hello.itemservice.domain.item.Item;
import hello.itemservice.domain.item.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.PostConstruct;
import java.util.List;

@Controller
@RequestMapping("/basic/items")
@RequiredArgsConstructor //final 을 받은 녀석을.. 자동으로 construct 를 만들어준다.
public class BasicItemController {

    private final ItemRepository itemRepository;

    @GetMapping
    public String items(Model model){
        List<Item> items = itemRepository.findAll();
        model.addAttribute("items",items);
        return "basic/items";
    }

    /**
     * 테스트용 데이터 추가
     */
    @PostConstruct
    public void init(){
        itemRepository.save(new Item("itemA", 10000, 10));
        itemRepository.save(new Item("itemB",20000, 20));
    }

    @GetMapping("/{itemId}")
    public String item(@PathVariable long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "basic/item";
    }

    @GetMapping("/add")
    public String addForm(){
        return "basic/addForm";
    }

    //v1
    //@PostMapping("/add")
    public String addItemV1(@RequestParam String itemName,
                       @RequestParam int price,
                       @RequestParam Integer quantity,
                       Model model){

        Item item = new Item();
        item.setItemName(itemName);
        item.setPrice(price);
        item.setQuantity(quantity);

        itemRepository.save(item);

        //model 에 넣었다.
        model.addAttribute("item", item);

        return "basic/item";
    }

    //v2
    //Name 속성 넣어줌.
    //ModelAttribute 기능 : Model 을 만든다.
    //@PostMapping("/add")
    public String addItemV2(@ModelAttribute("item") Item item){
        itemRepository.save(item);

        //model 에 넣었다.
        //model.addAttribute("item", item); //같은 이름으로 만들어 져서 , 자동 추가, 생략 가능
        //Model 에 들어가는 네이밍이 되기 떄문에 이름에 주의하자.

        return "basic/item";
    }

    //v3
    //Model Attribute 이름을 생략하면 ? -> 클래스의 명의 맨 앞의 문자를 소문자로 바꿔서 이름으로 지정한다.
    //ex> Hello 클래스 일 경우 -> hello 네이밍을 가짐
    //@PostMapping("/add")
    public String addItemV3(@ModelAttribute Item item){
        itemRepository.save(item);
        //model 에 넣었다.
        //model.addAttribute("item", item); //같은 이름으로 만들어 져서 , 자동 추가, 생략 가능
        //Model 에 들어가는 네이밍이 되기 떄문에 이름에 주의하자.
        return "basic/item";
    }

    //ModelAttribute 생략할 경우
    //Item -> item 으로 바뀌어서 모델에 담긴다
    //@PostMapping("/add")
    public String addItemV4(Item item){
        itemRepository.save(item);
        //model 에 넣었다.
        //model.addAttribute("item", item); //같은 이름으로 만들어 져서 , 자동 추가, 생략 가능
        //Model 에 들어가는 네이밍이 되기 떄문에 이름에 주의하자.
        return "basic/item";
    }

    /**
     * 마지막으로 한 행위가 .. 계쏙 된다.
     * post 만을 계속 호출 할 경우.
     * 새로 고침을 해도 .. 마지막으로 한 요청을 서버에 다시 요청하는것.
     * post/Redirect/Get -> PRG...
     * 문제점 : 아래와 같은 코드로 할경우 URL 인코딩이 되어있지 않기 때문에 해당 방법은 위험.
     */
    //@PostMapping("/add")
    public String addItemV5(Item item){
        itemRepository.save(item);
        return "redirect:/basic/items/" + item.getId();
    }

    //rediectAttribute에 세팅되면 아래와 같이 넣어줄 수 있따. / 나머지 애들은 쿼리 파라메터로 들어간다.
    @PostMapping("/add")
    public String addItemV6(Item item, RedirectAttributes redirectAttributes){
        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId",savedItem.getId());
        redirectAttributes.addAttribute("status",true);
        return "redirect:/basic/items/{itemId}";
    }

    @GetMapping("/{itemId}/edit")
    public String editForm(@PathVariable Long itemId, Model model){
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "basic/editForm";
    }

    @PostMapping("/{itemId}/edit")
    public String edit(@PathVariable Long itemId, @ModelAttribute Item item){
        itemRepository.update(itemId, item);

        //rediect 설정.
        return "redirect:/basic/items/{itemId}";
    }
}

    /**
     * 중요한 점
     * HTML Form 전송은 PUT, PATCH를 지원하지 않는다. Get, Post 만 사용할 수 있다
     * PUT, POST 는 HTTP API 전송시에 사용한다
     * 스프링에서 HTTP POST Form 요청할 때 히든 필드를 통해서 PUT, PATCH 매핑을 사용하는 방법이 있지만.. HTTP 요청상 POST 요청이다.
     */


