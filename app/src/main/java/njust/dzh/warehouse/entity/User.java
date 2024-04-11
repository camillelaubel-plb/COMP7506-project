package njust.dzh.warehouse.entity;

import java.io.Serializable;

//用户类，存放账号和密码以及权限
public class User implements Serializable {
    private String username;//用户名
    private String password;//密码
    private int power;//权限

    public User() {
    }

    public User(String username, String password, int power) {
        this.username = username;
        this.password = password;
        this.power = power;
    }

    public User(int power) {
        this.power = power;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getPower() {
        return power;
    }

    public void setPower(int power) {
        this.power = power;
    }

    @Override
    public String toString() {
        return "User{" + "username='" + username + '\''
                + ", password='" + password + '\'' +
                ", power=" + power + '}';
    }
}
