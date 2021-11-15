package main.modules.permission;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;

public class PermissionManager {
    /**
     * Check if user has specified permission
     * @param permission
     * @param member
     * @return
     */
    public static boolean hasPermission(Permission permission, Member member){
        try{
            return member.hasPermission(permission);
        } catch (NullPointerException e){
            return false;
        }
    }

    public static boolean canManageMessages(Member member){
        return hasPermission(Permission.MESSAGE_MANAGE, member);
    }

    public static boolean canManageRoles(Member member){
        return hasPermission(Permission.MANAGE_ROLES, member);
    }

    public static boolean canMute(Member member){
        return canManageRoles(member);
    }

    public static boolean isAdmin(Member member){
        return hasPermission(Permission.MANAGE_SERVER, member);
    }
}
