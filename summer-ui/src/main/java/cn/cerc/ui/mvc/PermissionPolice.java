package cn.cerc.ui.mvc;

import java.lang.annotation.Annotation;

import cn.cerc.core.Utils;
import cn.cerc.mis.core.Operators;
import cn.cerc.mis.core.Permission;

public class PermissionPolice {

    public final boolean allowGuestUser(String permission) {
        if (Permission.GUEST.length() > permission.length())
            return false;

        return permission.startsWith(Permission.GUEST);
    }

    public final boolean checkPassed(String permissions, String request) {
        if (Utils.isEmpty(permissions) || Utils.isEmpty(request))
            return true;

        if (request.equals(Permission.USERS))
            return !permissions.equals(Permission.GUEST);

        // 授权了ADMIN权限
        if (permissions.equals(Permission.ADMIN))
            return true;

        // 授权与要求的权限相同
        if (permissions.equals(request))
            return true;

        // 如果出现被限制的权限（以减号开头），反向检查
        for (String item : permissions.split(";")) {
            if (item.startsWith("-")) {
                if (compareMaster(request, item.substring(1)))
                    return false;
                if (compareDetail(request, item.substring(1)))
                    return false;
            }
        }

        // 正常检查
        for (String item : permissions.split(";")) {
            if (!item.startsWith("-")) {
                if (compareMaster(item, request))
                    return true;
                if (compareDetail(item, request))
                    return true;
            }
        }

        return false;
    }

    private final boolean compareMaster(String master, String request) {
        if (master.equals(Permission.ADMIN))
            return true;

        if (request.equals(Permission.GUEST) || request.equals(Permission.USERS) || request.equals(master))
            return true;

        // 支持用户的授权带*使用
        if (master.endsWith(".*")) {
            String flag = master.substring(0, master.length() - 2);
            if (request.length() >= flag.length()) {
                if (request.substring(0, flag.length()).equals(flag)) {
                    return true;
                }
            }
        }
        return false;
    }

    private final boolean compareDetail(String master, String request) {
        // 检查是否存在[]
        String masterText = master;
        int masterStart = master.indexOf("[");
        String[] masterDetail = {};
        if ((masterStart > 0) && master.endsWith("]")) {
            masterText = master.substring(0, masterStart);
            masterDetail = master.substring(masterStart + 1, master.length() - 1).split(",");
        }
        String childText = request;
        int childStart = request.indexOf("[");
        String[] childDetail = {};
        if ((childStart > 0) && request.endsWith("]")) {
            childText = request.substring(0, childStart);
            childDetail = request.substring(childStart + 1, request.length() - 1).split(",");
        }

        // 主体比较通过则继续比较
        if (compareMaster(masterText, childText)) {
            // 有任一方没有内容均视为通过
            if ((masterDetail.length == 0) || (childDetail.length == 0))
                return true;
            // 授权方仅有一个参数，且参数内容为*则视为通过
            if ((masterDetail.length) == 1 && (masterDetail[0].equals("*")))
                return true;

            // 比较detail
            boolean pass = true;
            for (String detail : childDetail) {
                if ((!"".equals(detail)) && (!inArray(detail, masterDetail))) {
                    pass = false;
                    break;
                }
            }
            if (pass)
                return true;
        }
        return false;
    }

    private final boolean inArray(final String value, final String[] list) {
        for (String item : list) {
            if (value.equals(item))
                return true;
        }
        return false;
    }

    public final String getPermission(Class<?> class1) {
        String permission = Permission.USERS;
        for (Annotation item : class1.getDeclaredAnnotations()) {
            if (item instanceof Permission) {
                permission = ((Permission) item).value();
                if ("".equals(permission))
                    permission = Permission.USERS;
            }
            if (item instanceof Operators) {
                StringBuffer sb = new StringBuffer(permission);
                sb.append("[");
                int count = 0;
                for (String detail : ((Operators) item).value()) {
                    if (count > 0)
                        sb.append(",");
                    sb.append(detail);
                    count++;
                }
                sb.append("]");
                permission = sb.toString();
            }
        }
        return permission;
    }

}
