package my.uum;

import java.sql.*;
import java.util.Arrays;

/**
 * This class is for connecting the database in SQLite to get the information that had been stored.
 *
 * @author Li Jia Ni
 */
public class Database {
    static Connection conn = null;

    /**
     * This is constructor to connect the app with SQLite.
     */
    public Database() {
        try {
            conn = DriverManager.getConnection("jdbc:sqlite:assignment2.db");
            System.out.println("Done");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method is use for checking the user who had registered or not.
     *
     * @param ic User's identity card number.
     * @return If registered user then return the user's staff id otherwise it will return fail in String type.
     */
    public static String loginUser(String ic) {
        String respond = "";
        String sql = "SELECT * FROM User WHERE icNo = '" + ic + "'";

        try {
            PreparedStatement preState = conn.prepareStatement(sql);
            ResultSet rs = preState.executeQuery();

            if (rs.next()) {
                respond = rs.getString("staffID");
            } else {
                respond = "fail";
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return respond;
    }

    /**
     * This method is for add new user into the User database.
     *
     * @param icNo  The user's identity card number.
     * @param id    The user's staff id. It will change to integer type.
     * @param name  The user's name.
     * @param phone The user's phone number.
     * @param email The user's email address.
     * @return Status of adding user into User database in String type.
     */
    public static String addUser(String icNo, String id, String name, String phone, String email) {
        String respond = "";
        int staffID = Integer.parseInt(id);
        String sql = "INSERT INTO User VALUES ('" + icNo + "' , " + staffID + ", '" + name + "', '" + phone + "', '" + email + "')";

        try {
            PreparedStatement preState = conn.prepareStatement(sql);
            int row = preState.executeUpdate();
            System.out.println(row);//check updated or not (row affected)

            if (row != 0) {
                respond = "Successfully Registered. Please provide your I/C number to login the system.";
            } else {
                respond = "Fail to register. Please provide your I/C number to login the system.";
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return respond;
    }

    /**
     * This method is for getting the room details from Room database.
     *
     * @return The room's id, description and the maximum capacity in String type.
     */
    public static String getRoom() {
        String respond = "";
        String sql = "select * from Room ORDER BY maxCap";

        try {
            PreparedStatement preState = conn.prepareStatement(sql);
            ResultSet rs = preState.executeQuery();

            while (rs.next()) {
                respond += "\nRoom ID: " + rs.getString("roomID") + "\n" +
                        "Description: " + rs.getString("roomDesc") + "\nMaximum capacity: " + rs.getString("maxCap") + "\n";
            }

            if (respond.equals("")) {
                respond += "";
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return respond;
    }

    /**
     * This method is for checking the available time for a specific room based on the date.
     *
     * @param date   The date of booking that select by user.
     * @param roomID The room for booking that select by user.
     * @return The available time for booking in String type.
     */
    public static String checkRoom(String date, String roomID) {
        String respond = "";
        String sql = "SELECT time FROM Booking WHERE date = '" + date + "' AND roomID = '" + roomID + "' ORDER BY time";
        String[] time = {"1", "2", "3", "4"};
        int count = 4;//represent the length of time cuz array cannot resize

        try {
            PreparedStatement preState = conn.prepareStatement(sql);
            ResultSet rs = preState.executeQuery();

            while (rs.next()) {
                if (rs.getString("time").equals("1")) {
                    int removeIndex = Arrays.asList(time).indexOf("1");
                    for (int i = removeIndex; i < time.length - 1; i++) {
                        time[i] = time[i + 1];
                    }
                    count--;
                } else if (rs.getString("time").equals("2")) {
                    int removeIndex = Arrays.asList(time).indexOf("2");
                    for (int i = removeIndex; i < time.length - 1; i++) {
                        time[i] = time[i + 1];
                    }
                    count--;
                } else if (rs.getString("time").equals("3")) {
                    int removeIndex = Arrays.asList(time).indexOf("3");
                    for (int i = removeIndex; i < time.length - 1; i++) {
                        time[i] = time[i + 1];
                    }
                    count--;
                } else if (rs.getString("time").equals(time[1])) { //check is it all time full
                    count = 0;
                } else if (rs.getString("time").equals("4")) {
                    count--;
                }
            }

            for (int i = 0; i < count; i++) {
                switch (time[i]) {
                    case "1":
                        respond += "\n1- 8:30a.m. - 10:00a.m.";
                        break;
                    case "2":
                        respond += "\n2- 10:30a.m. - 12:30p.m.";
                        break;
                    case "3":
                        respond += "\n3- 1:00p.m. - 3:00p.m.";
                        break;
                    case "4":
                        respond += "\n4- 3:30p.m. - 5:00p.m.";
                        break;
                }
            }

            if (count == 0 || respond.equals("")) {
                respond = "fail";
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return respond;
    }

    /**
     * This method is for add a new booking into Booking database.
     *
     * @param purpose The purpose for booking this room.
     * @param date    The date that select by user.
     * @param time    The slot time that select by user.
     * @param roomID  The room that select by user.
     * @param id      User's staff id. It will change to integer type.
     * @return Status of adding booking into Booking database in String type.
     */
    public static String addBooking(String purpose, String date, String time, String roomID, String id) {
        String respond = "";
        int staffID = Integer.parseInt(id);
        String sql = "INSERT INTO Booking VALUES ('" + purpose + "','" + date + "','" + time + "','" + roomID + "'," + staffID + ")";

        try {
            PreparedStatement preState = conn.prepareStatement(sql);
            int row = preState.executeUpdate();
            System.out.println(row);//check updated or not (row affected)

            if (row != 0) {
                respond = "Successfully booking. You can check the booking with /list command.";
            } else {
                respond = "fail";
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return respond;
    }

    /**
     * This method is for getting the booking list booked by a specific user.
     *
     * @param id User's staff id. It will change to integer type.
     * @return The list of booking detail with room information in String type.
     */
    public static String getBooking(String id) {
        String respond = "";
        int staffID = Integer.parseInt(id);
        String sql = "SELECT Booking.roomID AS roomID, Room.roomDesc AS roomDesc, Room.maxCap AS maxCap, Booking.date AS date, Booking.time AS time FROM Booking INNER JOIN Room ON Booking.roomID = Room.roomID INNER JOIN User ON Booking.staffID = User.staffID WHERE Booking.staffID = " + staffID + " ORDER BY Booking.date";

        try {
            PreparedStatement preState = conn.prepareStatement(sql);
            ResultSet rs = preState.executeQuery();

            while (rs.next()) {
                respond += "\nRoom Id: " + rs.getString("roomID") + "\nDescription: " + rs.getString("roomDesc") +
                        "\nMaximum capacity: " + rs.getString("maxCap") + "\nBooking date and time: " + rs.getString("date") + "\t";

                switch (rs.getString("time")) {
                    case "1":
                        respond += "8:30a.m. - 10:00a.m.\n";
                        break;
                    case "2":
                        respond += "10:30a.m. - 12:30p.m.\n";
                        break;
                    case "3":
                        respond += "1:00p.m. - 3:00p.m.\n";
                        break;
                    case "4":
                        respond += "3:30p.m. - 5:00p.m.\n";
                        break;
                }
            }

            if (respond.equals("")) {
                respond = "fail";
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return respond;
    }

    /**
     * This method is for checking the available time to cancel for a specific room and specific user.
     *
     * @param roomID The room id to cancel.
     * @param id     User's staff id. It will change to integer type.
     * @return The list of available time to cancel in String type.
     */
    public static String checkCancel(String roomID, String id) {
        String respond = "";
        int staffID = Integer.parseInt(id);
        String sql = "SELECT * FROM Booking WHERE staffID = " + staffID + " AND roomID = '" + roomID + "' ORDER BY roomID";

        try {
            PreparedStatement preState = conn.prepareStatement(sql);
            ResultSet rs = preState.executeQuery();

            while (rs.next()) {
                respond += "\n" + rs.getString("time") + "- " + rs.getString("date") + "\t";
                switch (rs.getString("time")) {
                    case "1":
                        respond += "8:30a.m. - 10:00a.m.";
                        break;
                    case "2":
                        respond += "10:30a.m. - 12:30p.m.";
                        break;
                    case "3":
                        respond += "1:00p.m. - 3:00p.m.";
                        break;
                    case "4":
                        respond += "3:30p.m. - 5:00p.m.";
                        break;
                }
            }

            if (respond.equals("")) {
                respond = "fail";
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return respond;
    }

    /**
     * This method is for delete the booking in the Booking database.
     *
     * @param date   The date that user select to cancel.
     * @param time   The time that user select to cancel.
     * @param roomID The room id that user select to cancel.
     * @param id     User's staff id. It will change to integer type.
     * @return Status of cancel the booking in String type.
     */
    public static String cancelRoom(String date, String time, String roomID, String id) {
        String respond = "";
        int staffID = Integer.parseInt(id);
        String sql = "DELETE FROM Booking WHERE staffID = " + staffID + " AND roomID = '" + roomID + "' AND date = '" + date + "' AND time = '" + time + "'";


        try {
            PreparedStatement preState = conn.prepareStatement(sql);
            int row = preState.executeUpdate();
            System.out.println(row);//check updated or not (row affected)

            if (row != 0) {
                respond = "Successfully cancel the room.\nYou can continue your journey with the command in the menu.";
            } else {
                respond = "fail";
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return respond;
    }
}
