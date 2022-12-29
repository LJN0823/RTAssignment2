package my.uum;

/**
 * This class is for saving the user's information.
 *
 * @author Li Jia Ni
 */
public class User {
    private String chatID;
    private String staffID;
    private String step;
    private String icNo;
    private String name;
    private String phone;
    private String email;

    /**
     * This method is for getting the user's chat id.
     *
     * @return User's chat id.
     */
    public String getChatID() {
        return chatID;
    }

    /**
     * This method is for setting the user's chat id.
     *
     * @param chatID The new chat id.
     */
    public void setChatID(String chatID) {
        this.chatID = chatID;
    }

    /**
     * This method is for getting the user's staff id.
     *
     * @return User's staff id.
     */
    public String getStaffID() {
        return staffID;
    }

    /**
     * This method is for setting the user's staff id.
     *
     * @param staffID The new staff id.
     */
    public void setStaffID(String staffID) {
        this.staffID = staffID;
    }

    /**
     * This method is for getting the user's step in system.
     *
     * @return User's step.
     */
    public String getStep() {
        return step;
    }

    /**
     * This method is for setting the user's step in the system.
     *
     * @param step The new step.
     */
    public void setStep(String step) {
        this.step = step;
    }

    /**
     * This method is for getting the user's identity card number.
     *
     * @return User's identity card number.
     */
    public String getIcNo() {
        return icNo;
    }

    /**
     * This method is for setting the user's identity card number.
     *
     * @param icNo The new identity card number.
     */
    public void setIcNo(String icNo) {
        this.icNo = icNo;
    }

    /**
     * This method is for getting the user's name.
     *
     * @return User's name.
     */
    public String getName() {
        return name;
    }

    /**
     * This method is for setting the user's name.
     *
     * @param name The new name.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * This method is for getting the user's phone number.
     *
     * @return User's phone number.
     */
    public String getPhone() {
        return phone;
    }

    /**
     * This method is for setting the user's phone number.
     *
     * @param phone The new phone number.
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * This method is for getting the user's email address.
     *
     * @return User's email address.
     */
    public String getEmail() {
        return email;
    }

    /**
     * This method is for setting the user's email address.
     *
     * @param email The new email address.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * This method is use for displaying the user's information.
     *
     * @return User information in String type.
     */
    @Override
    public String toString() {
        return "User{" +
                "chatID='" + chatID + '\'' +
                ", staffID='" + staffID + '\'' +
                ", step='" + step + '\'' +
                ", icNo='" + icNo + '\'' +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                '}';
    }

}
