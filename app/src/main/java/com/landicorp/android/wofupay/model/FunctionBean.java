package com.landicorp.android.wofupay.model;

import java.util.List;

/**
 * Description：功能信息使用
 *
 * @Author：桑小年
 * @Data：2016/9/22 16:09
 */
public class FunctionBean {


    /**
     * code : 0000
     * Msg : 成功
     * Data : [{"id":14,"menuid":1,"terminalcode":"99026428","menustatus":"00","msg":"正常"},{"id":15,"menuid":2,"terminalcode":"99026428","menustatus":"02","msg":"系统服务维护，暂停服务"},{"id":16,"menuid":3,"terminalcode":"99026428","menustatus":"00","msg":"正常"},{"id":17,"menuid":4,"terminalcode":"99026428","menustatus":"00","msg":"正常"},{"id":18,"menuid":5,"terminalcode":"99026428","menustatus":"02","msg":"正常"},{"id":19,"menuid":7,"terminalcode":"99026428","menustatus":"00","msg":"正常"},{"id":20,"menuid":8,"terminalcode":"99026428","menustatus":"01","msg":"系统升级"},{"id":21,"menuid":9,"terminalcode":"99026428","menustatus":"00","msg":"正常"},{"id":22,"menuid":10,"terminalcode":"99026428","menustatus":"00","msg":"正常"},{"id":23,"menuid":11,"terminalcode":"99026428","menustatus":"02","msg":"系统服务维护，暂停服务"},{"id":24,"menuid":12,"terminalcode":"99026428","menustatus":"00","msg":"正常"},{"id":25,"menuid":13,"terminalcode":"99026428","menustatus":"00","msg":"正常"},{"id":26,"menuid":14,"terminalcode":"99026428","menustatus":"00","msg":"正常"}]
     */

    private String code;
    private String Msg;
    /**
     * id : 14
     * menuid : 1 
     * terminalcode : 99026428
     * menustatus : 00
     * msg : 正常
     */

    private List<DataBean> Data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return Msg;
    }

    public void setMsg(String Msg) {
        this.Msg = Msg;
    }

    public List<DataBean> getData() {
        return Data;
    }

    public void setData(List<DataBean> Data) {
        this.Data = Data;
    }

    public static class DataBean {
        private int id;
        private int menuid;
        private String terminalcode;
        private String menustatus="00";
        private String msg;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getMenuid() {
            return menuid;
        }

        public void setMenuid(int menuid) {
            this.menuid = menuid;
        }

        public String getTerminalcode() {
            return terminalcode;
        }

        public void setTerminalcode(String terminalcode) {
            this.terminalcode = terminalcode;
        }

        public String getMenustatus() {
            return menustatus;
        }

        public void setMenustatus(String menustatus) {
            this.menustatus = menustatus;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

		@Override
		public String toString() {
			return "DataBean [id=" + id + ", menuid=" + menuid
					+ ", terminalcode=" + terminalcode + ", menustatus="
					+ menustatus + ", msg=" + msg + "]";
		}
        
    }

	@Override
	public String toString() {
		return "FunctionBean [code=" + code + ", Msg=" + Msg + ", Data=" + Data
				+ "]";
	}
    
    
    
    
}
