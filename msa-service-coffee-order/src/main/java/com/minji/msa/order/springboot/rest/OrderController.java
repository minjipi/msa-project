package com.minji.msa.order.springboot.rest;

import com.minji.msa.order.domain.vo.OrderVO;
import com.minji.msa.order.springboot.feign.IMemberClient;
import com.minji.msa.order.springboot.service.OrderServiceImpl;
import com.minji.msa.order.system.common.ResponseMetaVO;
import com.minji.msa.order.system.common.ResponseVO;
import com.minji.msa.order.system.queue.KafkaSendMessage;
import com.minji.msa.order.system.queue.KafkaProducer;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@CrossOrigin
@RequestMapping("/api/v1/order")
@RestController
@RefreshScope
@Api(value = "OrderController")
public class OrderController {

    private Logger logger = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    MessageSource messageSource;

    @Autowired
    OrderServiceImpl orderServiceImpl;

    @Qualifier("com.minji.msa.order.springboot.feign.IMemberClient")
    @Autowired
    IMemberClient iMemberClient;

    @Autowired
    private KafkaProducer kafkaProducer;

    @Value("${propery_owner}")
    private String propery_owner;

    @ApiOperation(value="", notes="전체조회", response = ResponseVO.class)
    @ApiResponses(value={
            @ApiResponse(code=200, message = "Successfully retrieved list"),
            @ApiResponse(code=401, message = "You ar not authorized to view the resource"),
            @ApiResponse(code=403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code=404, message = "The resource you were trying to reach is not found")
    })
    @GetMapping
    public ResponseEntity<ResponseVO> getAllOrder(){

        logger.debug("############# propery_owner : " + propery_owner + " ####################");

        //결과 객체선언
        ResponseVO responseVO = new ResponseVO();
        ResponseMetaVO responseMetaVO = new ResponseMetaVO();

        //전체 count 조회
        responseMetaVO.setAllRecordCount(Long.bitCount(orderServiceImpl.getAllOrderCount()));

        //조회
        List<OrderVO> orderVOList =
                orderServiceImpl.getAllOrder("1", "10", "desc", "_id");

        //결과객체 리턴
        responseVO.setResultVO(orderVOList);
        responseVO.setResponseMetaVO(responseMetaVO);
        return new ResponseEntity<ResponseVO>(responseVO, HttpStatus.OK);
    }

    @ApiOperation(value="", notes="단건 저장<br>_id 값은 자동생성됨", response = ResponseVO.class)
    @ApiResponses(value={
            @ApiResponse(code=200, message = "Successfully retrieved list"),
            @ApiResponse(code=401, message = "You ar not authorized to view the resource"),
            @ApiResponse(code=403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code=404, message = "The resource you were trying to reach is not found"),
    })

//    public ResponseEntity<ResponseVO> addOrderFallback(@RequestBody OrderVO orderVO) throws Exception {
//        logger.debug("=====>>>>> addOrderFallback method executed! <<<<<==================");
//        return new ResponseEntity<ResponseVO>(new ResponseVO(), HttpStatus.OK);
//    }
//    @HystrixCommand(fallbackMethod = "addOrderFallback")
    @PostMapping
    public ResponseEntity<ResponseVO> addOrder(@RequestBody OrderVO orderVO) throws Exception {

        //결과 객체선언
        ResponseVO responseVO = new ResponseVO();
        ResponseMetaVO responseMetaVO = new ResponseMetaVO();

        //        //Member 조회
//        String userName = iMemberClient.getUseById(orderVO.getUserId());
//        logger.debug("Member 조회 getUseById : 결과 userName ===> "
//                + userName);

        //Member 조회
        String _id = iMemberClient.getUseByUserName(orderVO.getUserName());
        logger.debug("Member 조회 getUseByUserName : 결과 _id ===> "
                + _id);

        //입력
        orderVO.set_id(UUID.randomUUID().toString());

        if(_id.equals("fallback")){
            orderVO.setUserName("ANONYMOUS");
        }else{
            orderVO.setUserId(_id);
        }

        OrderVO resultOrderVO = orderServiceImpl.addOrder(orderVO);

        //건수
        responseMetaVO.setAllRecordCount(1);








        //Kafka Queue 전송
        KafkaSendMessage kafkaSendMessage = new KafkaSendMessage();
        kafkaSendMessage.set_id(orderVO.get_id());
        kafkaSendMessage.setOrderId(orderVO.get_id());
        kafkaSendMessage.setOrderName(orderVO.getOrderName());
        kafkaSendMessage.setUserId(orderVO.getUserId());
        kafkaSendMessage.setUserName(orderVO.getUserName());

        kafkaProducer.send("minji-kafka-test", kafkaSendMessage);


        //결과객체 리턴
        responseVO.setResponseMetaVO(responseMetaVO);
        responseVO.setResultVO(resultOrderVO);
        return new ResponseEntity<ResponseVO>(responseVO, HttpStatus.OK);
    }

    @ApiOperation(value="", notes="단건 수정<br>_id 값은 필수입력<br>_id 값 기준으로 업데이트함"
            , response = ResponseVO.class)
    @ApiResponses(value={
            @ApiResponse(code=200, message = "Successfully retrieved list"),
            @ApiResponse(code=401, message = "You ar not authorized to view the resource"),
            @ApiResponse(code=403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code=404, message = "The resource you were trying to reach is not found"),
    })
    @PutMapping
    public ResponseEntity<ResponseVO> modifyOrder(@RequestBody OrderVO orderVO){

        //결과 객체선언
        ResponseVO responseVO = new ResponseVO();
        ResponseMetaVO responseMetaVO = new ResponseMetaVO();

        //수정
        OrderVO resultOrderVO = orderServiceImpl.modifyOrder(orderVO);

        //건수
        responseMetaVO.setAllRecordCount(1);

        //결과객체 리턴
        responseVO.setResponseMetaVO(responseMetaVO);
        responseVO.setResultVO(resultOrderVO);

        return new ResponseEntity<ResponseVO>(responseVO, HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<ResponseVO> removeOrder(@RequestBody List<String> _idList) throws Exception {

        //결과 객체선언
        ResponseVO responseVO = new ResponseVO();
        ResponseMetaVO responseMetaVO = new ResponseMetaVO();

        //삭제
        int cnt =_idList.size();
        List<OrderVO> orderVOList = new ArrayList<>();
        for (int i=0; i< cnt; i++){
            OrderVO orderVO = new OrderVO();
            orderVO.set_id(_idList.get(i));
            orderVOList.add(orderVO);
        }

        orderServiceImpl.removeOrder(orderVOList);

        //건수
        responseMetaVO.setAllRecordCount(_idList.size());

        //결과객체 리턴
        responseVO.setResponseMetaVO(responseMetaVO);
        responseVO.setResultVO("");
        return new ResponseEntity<ResponseVO>(responseVO, HttpStatus.OK);
    }
}
