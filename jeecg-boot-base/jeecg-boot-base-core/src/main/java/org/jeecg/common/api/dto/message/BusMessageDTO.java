package org.jeecg.common.api.dto.message;

import lombok.Data;

import java.io.Serializable;

/**
 * 带业务参数的消息
 * todo 4.15
 * 同样是DTO，作为controller方法的入参
 * 标注可序列化
 * 同样在service解析转化为其他的实体类
 */
@Data
public class BusMessageDTO extends MessageDTO implements Serializable {

    private static final long serialVersionUID = 9104793287983367669L;
    /**
     * 业务类型
     */
    private String busType;

    /**
     * 业务id
     */
    private String busId;

    public BusMessageDTO(){

    }

    /**
     * 构造 带业务参数的消息
     * @param fromUser
     * @param toUser
     * @param title
     * @param msgContent
     * @param msgCategory
     * @param busType
     * @param busId
     */
    public BusMessageDTO(String fromUser, String toUser, String title, String msgContent, String msgCategory, String busType, String busId){
        super(fromUser, toUser, title, msgContent, msgCategory);
        this.busId = busId;
        this.busType = busType;
    }
}
