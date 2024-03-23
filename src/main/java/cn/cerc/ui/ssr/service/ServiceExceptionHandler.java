package cn.cerc.ui.ssr.service;

public class ServiceExceptionHandler {

    private Exception exception;

    public void setException(Exception exception) {
        this.exception = exception;
    }

    public boolean existException() {
        return exception != null;
    }

    public void throwsException() throws Exception {
        throw exception;
    }

}
