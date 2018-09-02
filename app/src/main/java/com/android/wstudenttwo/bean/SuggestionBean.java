package com.android.wstudenttwo.bean;

public class SuggestionBean {

    /**
     * comf : {"type":"comf","brf":"较舒适","txt":"白天有降雨，但会使人们感觉有些热，不过大部分人仍会有比较舒适的感觉。"}
     * sport : {"type":"sport","brf":"较不宜","txt":"有降水，且风力较强，推荐您在室内进行低强度运动；若坚持户外运动，请选择避雨防风的地点。"}
     * cw : {"type":"cw","brf":"不宜","txt":"不宜洗车，未来24小时内有雨，如果在此期间洗车，雨水和路上的泥水可能会再次弄脏您的爱车。"}
     */

    public ComfBean comf;
    public SportBean sport;
    public CwBean cw;

    public ComfBean getComf() {
        return comf;
    }

    public void setComf(ComfBean comf) {
        this.comf = comf;
    }

    public SportBean getSport() {
        return sport;
    }

    public void setSport(SportBean sport) {
        this.sport = sport;
    }

    public CwBean getCw() {
        return cw;
    }

    public void setCw(CwBean cw) {
        this.cw = cw;
    }

    public static class ComfBean {
        /**
         * type : comf
         * brf : 较舒适
         * txt : 白天有降雨，但会使人们感觉有些热，不过大部分人仍会有比较舒适的感觉。
         */

        public String type;
        public String brf;
        public String txt;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getBrf() {
            return brf;
        }

        public void setBrf(String brf) {
            this.brf = brf;
        }

        public String getTxt() {
            return txt;
        }

        public void setTxt(String txt) {
            this.txt = txt;
        }
    }

    public static class SportBean {
        /**
         * type : sport
         * brf : 较不宜
         * txt : 有降水，且风力较强，推荐您在室内进行低强度运动；若坚持户外运动，请选择避雨防风的地点。
         */

        public String type;
        public String brf;
        public String txt;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getBrf() {
            return brf;
        }

        public void setBrf(String brf) {
            this.brf = brf;
        }

        public String getTxt() {
            return txt;
        }

        public void setTxt(String txt) {
            this.txt = txt;
        }
    }

    public static class CwBean {
        /**
         * type : cw
         * brf : 不宜
         * txt : 不宜洗车，未来24小时内有雨，如果在此期间洗车，雨水和路上的泥水可能会再次弄脏您的爱车。
         */

        public String type;
        public String brf;
        public String txt;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getBrf() {
            return brf;
        }

        public void setBrf(String brf) {
            this.brf = brf;
        }

        public String getTxt() {
            return txt;
        }

        public void setTxt(String txt) {
            this.txt = txt;
        }
    }
}
