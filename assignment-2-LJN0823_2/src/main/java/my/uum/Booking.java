package my.uum;

/**
 * This class is for saving the booking's information based on the specific user.
 *
 * @author Li Jia Ni
 */
public class Booking extends User{
    private String purpose;
    private String date;
    private String time;
    private String roomID;
    private String chatID;

    /**
     * This is constructor to set the room id from user's command.
     *
     * @param chatID The new room id for booking.
     */
    public Booking (String chatID){ //(String roomID) {
        this.chatID = chatID;
        //this.roomID = roomID;
    }

    /**
     * This method is for getting the booking's purpose.
     *
     * @return The booking's purpose.
     */
    public String getPurpose() {
        return purpose;
    }

    /**
     * This method is for setting the booking's purpose.
     *
     * @param purpose The new purpose to booking.
     */
    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    /**
     * This method is for getting the booking's date.
     *
     * @return The booking's date.
     */
    public String getDate() {
        return date;
    }
    /**
     * This method is for setting the booking's date.
     *
     * @param date The new date to booking.
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     * This method is for getting the booking's time.
     *
     * @return The booking's time.
     */
    public String getTime() {
        return time;
    }

    /**
     * This method is for setting the booking's time.
     *
     * @param time The new time to booking.
     */
    public void setTime(String time) {
        this.time = time;
    }

    /**
     * This method is for getting the booking's room id.
     *
     * @return The booking's room id.
     */
    public String getRoomID() {
        return roomID;
    }

    /**
     * This ethod is for setting the booking's room id.
     *
     * @param roomID The new room id to booking.
     */
    public void setRoomID(String roomID) {
        this.roomID = roomID;
    }

    /**
     * This method is use for displaying the booking's information.
     *
     * @return User information in String type.
     */

    @Override
    public String getChatID() {
        return chatID;
    }

    @Override
    public String toString() {
        return "Booking{" +
                "purpose='" + purpose + '\'' +
                ", date='" + date + '\'' +
                ", time='" + time + '\'' +
                ", roomID='" + roomID + '\'' +
                ", chatID='" + chatID + '\'' +
                '}';
    }
}
