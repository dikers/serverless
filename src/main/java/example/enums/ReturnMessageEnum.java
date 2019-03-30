package example.enums;

/**
 * @author: dikers
 * @creation: 2019/3/29
 * @notes:
 */
public enum ReturnMessageEnum {
    SUCCESS(0, "成功"),
    PARAM_ERROR(1, "参数异常"),
    PARAM_NULL(2, "入参为空"),
    SYSTEM_EXCEPTION(999, "未知异常");

    private String name;
    private int code;

    ReturnMessageEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getCode() {
        return code;
    }
}
