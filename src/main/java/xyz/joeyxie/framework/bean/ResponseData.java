package xyz.joeyxie.framework.bean;

/**
 * 用于保存Action方法返回的纯数据对象，当Action方法不需要跳转到某个页面，而是只返回数据(例如JSON)时，用该对象保存
 * Created by joey on 2016/1/17.
 */
public class ResponseData {

    // 模型数据
    private Object responseData;

    public ResponseData(Object responseData) {
        this.responseData = responseData;
    }

    public Object getResponseData() {
        return responseData;
    }
}
