package aps_bdsocket;

import java.io.Serializable;

public class Resposta implements Serializable {

    String Total;
    String Msg;

    public Resposta(String total, String Msg) {
        this.Total = total;
        this.Msg = Msg;
    }
    
    public String getTotal() {
        return Total;
    }

    public void setTotal(String total) {
        this.Total = total;
    }

    public String getMsg() {
        return Msg;
    }

    public void setMsg(String msg) {
        this.Msg = msg;
    }
    
    Resposta() {}

}
