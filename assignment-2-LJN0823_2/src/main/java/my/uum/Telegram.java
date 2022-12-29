package my.uum;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;

/**
 * This class is for control the user's command via Telegram Bot.
 *
 * @author Li Jia Ni
 */
public class Telegram extends TelegramLongPollingBot {

    ArrayList<User> users = new ArrayList<>();
    ArrayList<Booking> books = new ArrayList<>();
    Booking book = null;

    /**
     * This method is for respond to user based on their command.
     *
     * @param update The latest message information from user.
     */
    @Override
    public void onUpdateReceived(Update update) {
        String command = update.getMessage().getText();//user type
        String msg;//text bot to user
        SendMessage response = new SendMessage();//function send msg to user
        User user = null;

        if (update.hasMessage()) {
            String chatID = update.getMessage().getChatId().toString();
            response.setChatId(chatID);
            user = saveUser(chatID); //check user's step by chat id
            book = updateBook(chatID);
        }

        switch (command) {
            case "/start":
                msg = "This is a system use for booking room that available in UUM.\n" +
                        "Please provide your identity card number (I/C No) without - to login the system. For example 00082301xxxx";
                response.setText(msg);
                user.setStep("login");
                user.setStaffID("");
                user.setEmail("");
                user.setName("");
                user.setPhone("");

                try {
                    execute(response);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
                break;

            case "/booking":
                if (!user.getStep().equals("successlogin")) {
                    msg = "Please provide your I/C number for login purposes or make sure follow the step to quit the activity.";
                } else {
                    msg = "Please enter the room id to has the booking. The available rooms for booking: " + Database.getRoom() + "\n" +
                            "\nIf not to booking anymore please command /quit to quit the action.";
                    user.setStep("booking");
                }
                response.setText(msg);

                try {
                    execute(response);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
                break;

            case "/list":
                if (!(user.getStep().equals("successlogin"))) {
                    msg = "Please provide your I/C number for login purposes or make sure follow the step to quit the activity.";
                } else {
                    String listResponse = Database.getBooking(user.getStaffID());
                    if (listResponse.equals("fail")) {
                        msg = "Haven't booking any room before. Command /booking to have your booking now.";
                    } else {
                        msg = user.getStaffID() + " here is your booking list:" + listResponse;
                        user.setStep("successlogin");
                    }
                }
                response.setText(msg);

                try {
                    execute(response);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
                break;

            case "/cancel":
                if (!(user.getStep().equals("successlogin"))) {
                    msg = "Please provide your I/C number for login purposes or make sure follow the step to quit the activity.";
                } else {
                    msg = "To confirm the cancellation please follow the instruction to provide your booked room's detail.\n" +
                            "Please provide your Room Id. (You can command /list after you /quit to review your booked room's detail.)\n" +
                            "\nIf not to cancel anymore please command /quit to quit the action.";
                    user.setStep("checkcancel");
                }
                response.setText(msg);

                try {
                    execute(response);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
                break;

            case "/logout":
                msg = "Log out successfully! If want to login again, kindly command /start to login again.";
                user.setStep(" ");
                response.setText(msg);

                try {
                    execute(response);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
                break;

            case "/quit": //only login user will notice this command
                msg = "Successfully quit the activity. You may continue your journey with the command:\n" +
                        "/booking - to book a room.\n/list - to list the room had book before.\n/cancel - to cancel the booked room.";
                response.setText(msg);
                user.setStep("successlogin");

                try {
                    execute(response);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
                break;

            default:
                if (user.getStep().equals("login")) {
                    String icNo = command;
                    user.setIcNo(icNo);
                    String loginResponse = Database.loginUser(icNo);

                    if (loginResponse.equals("fail")) {
                        msg = "Welcome for new user, make sure the I/C number is correct then kindly provide your staff id to continue the registration.\n" +
                                "If the I/C No is incorrect, please command /start to login again.\n" +
                                "Your I/C No : " + icNo;
                        user.setStep("register");
                    } else {
                        msg = "Successfully Login!";
                        user.setStaffID(loginResponse);
                        user.setStep("successlogin");
                    }
                    response.setText(msg);

                    try {
                        execute(response);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                } else if (user.getStep().equals("register")) {
                    msg = registerStatus(user, command);
                    response.setText(msg);

                    try {
                        execute(response);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                } else if (user.getStep().equals("booking")) {
                    String roomID = command;
                    book.setRoomID(roomID);
                    //book = updateBook(user, book);//new Booking(user.getChatID());
                    System.out.println(book.toString());
                    book.setPurpose("");
                    book.setTime("");
                    book.setDate("");
                    user.setStep("checkroom");
                    msg = "Please enter your booking date with the format -> yyyy-mm-dd <-. For example, 2023-01-23.";
                    response.setText(msg);

                    try {
                        execute(response);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                } else if (user.getStep().equals("checkroom")) {
                    String date = command;
                    String availableTime = Database.checkRoom(date, book.getRoomID());

                    if (availableTime.equals("fail")) {
                        msg = "All the time slot on " + date + " unavailable anymore. To continue the booking kindly enter /booking in the chat.";
                        user.setStep("successlogin");
                    } else {
                        book.setDate(date);
                        msg = "Please select the number that you prefer for the booking time. For example, 2.\n" +
                                "The available time for this room :\n" + availableTime + "\n\nIf want to cancel the booking action, please command /quit to cancel.";
                        user.setStep("bookroom");
                        book.setTime("");
                        book.setPurpose("");
                    }
                    response.setText(msg);

                    try {
                        execute(response);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                } else if (user.getStep().equals("bookroom")) {
                    msg = bookingStatus(command, book, user);
                    response.setText(msg);

                    try {
                        execute(response);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                } else if (user.getStep().equals("checkcancel")) {
                    String roomID = command;
                    String availableBooked = Database.checkCancel(roomID, user.getStaffID());
                    if (availableBooked.equals("fail")) {
                        msg = "There are no booked for this room. Please type again the booked room id to cancel the booking.\n" +
                                "If don't want to cancel, please command /quit to quit the cancellation.";
                    } else {
                        msg = "There are the booked datetime for this room:\n" + availableBooked + "\n\nPlease enter the number for the TIME that you want to cancel. \n" +
                                "For example, 1.(Which is indicate you want to cancel the booking on 8:30a.m. - 10:00a.m.)";
                        book = new Booking(roomID);
                        book.setDate("");
                        book.setTime("");
                        user.setStep("cancel");
                    }
                    response.setText(msg);

                    try {
                        execute(response);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                } else if (user.getStep().equals("cancel")) {
                    msg = "Wrong command";
                    if (book.getTime().equals("")) {
                        book.setTime(command);
                        msg = "Please provide the date that you want to cancel with the format -> yyyy-mm-dd <-. For example, 2023-01-23.";
                    } else if (book.getDate().equals("")) {
                        book.setDate(command);
                        String cancelRespond = Database.cancelRoom(book.getDate(), book.getTime(), book.getRoomID(), user.getStaffID());
                        if (cancelRespond.equals("fail")) {
                            msg = "There are no booked for this room. Please type again the booked room id to cancel the booking.\n" +
                                    "If don't want to cancel, please command /quit to quit the cancellation.";
                            user.setStep("checkcancel");
                        } else {
                            msg = cancelRespond;
                            user.setStep("successlogin");
                        }
                    }
                    response.setText(msg);

                    try {
                        execute(response);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                } else {
                    msg = "Could not understand this command. You may /start again this system.";
                    response.setText(msg);

                    try {
                        execute(response);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                }
        }
    }
    /**
     * bookcheck = new book
     * for(Booking book:books){
     *     if(book.getRoomID().equals(bookcheck.getRoomID()) && book.getDate().equals(bookcheck.getDate() && book.getTime().equals(cookcheck.getTime()))
     * }
     */
    private Booking updateBook(String chatid) {
        for (Booking book : books) {
            if (book.getChatID().equals(chatid)) {
                return book;
            }
        }
        Booking book = new Booking(chatid);
        books.add(book);

        return book;
    }

    /**
     * This method is for control the booking activity.
     *
     * @param command The latest message from user.
     * @param book    The booking's information.
     * @param user    The user's information.
     * @return Status after getting the command from user.
     */
    private String bookingStatus(String command, Booking book, User user) {
        String status = "";
        System.out.println(book.toString());

        if (book.getTime().equals("")) {
            book.setTime(command);
            status = "Please briefly describe your purpose of booking this room.";
        } else if (book.getPurpose().equals("")) {
            book.setPurpose(command);
            status = Database.addBooking(book.getPurpose(), book.getDate(), book.getTime(), book.getRoomID(), user.getStaffID());
            if (status.equals("fail")) {
                status = "Fail to booking the room. Please enter /booking to try again.";
                user.setStep("successlogin");
            } else {
                user.setStep("successlogin");
            }
        }
        return status;
    }

    /**
     * This method is for control the register activity.
     *
     * @param user    The user's information.
     * @param command The latest message from user.
     * @return Status after getting the command from user.
     */
    private String registerStatus(User user, String command) {
        String status = "";

        if (user.getStaffID().equals("")) {
            user.setStaffID(command);
            status = "Please provide your name.";
        } else if (user.getName().equals("")) {
            user.setName(command);
            status = "Please provide your phone number.";
        } else if (user.getPhone().equals("")) {
            user.setPhone(command);
            status = "Please provide your email address.";
        } else if (user.getEmail().equals("")) {
            user.setEmail(command);
            status = Database.addUser(user.getIcNo(), user.getStaffID(), user.getName(), user.getPhone(), user.getEmail());
            user.setStep("login");
        }
        return status;
    }

    /**
     * This method is for control the user step.
     *
     * @param chatID The user's id while chatting with telegram bot.
     * @return The user information such as their step and chat id.
     */
    private User saveUser(String chatID) { //save user chat id to control step
        System.out.println("before add user" + users.toString());
        for (User user : users) {
            if (user.getChatID().equals(chatID)) {
                return user;
            }
        }
        User user = new User();
        user.setChatID(chatID);
        users.add(user);//multiple user in a time
        return user;
    }

    /**
     * This method is for getting the owner's bot name.
     *
     * @return Telegram bot name.
     */
    @Override
    public String getBotUsername() {
        return "s278830_A221_bot";
    }

    /**
     * This method is for getting the secret token use to register telegram bot.
     *
     * @return Token of specific telegram bot.
     */
    @Override
    public String getBotToken() {
        return "5957285082:AAGaG55QPpNWD3rSd8ZS0WU0f1o0JszJMWU";
    }
}
