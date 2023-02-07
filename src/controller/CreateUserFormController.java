package controller;

import db.DBConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.util.Random;

public class CreateUserFormController {
    public Label txtQuote;
    public TextField txtUserName;
    public TextField txtEmail;
    public Label lblPasswordMissMatch;
    public AnchorPane rootCreateUser;
    public Label lblUserId;
    public PasswordField pwdPassword;
    public PasswordField pwdConfirmPassword;


    String username;
    String password;
    String confirmPassword;
    String email;
    String userId;

    String[] motive={"When you have a dream, you've got to grab it and never let go.",
            "Nothing is impossible. The word itself says 'I'm possible!",
            "There is nothing impossible to they who will try",
            "The bad news is time flies. The good news is you're the pilot",
            "Life has got all those twists and turns. You've got to hold on tight and off you go",
            "Keep your face always toward the sunshine, and shadows will fall behind you.",
            "Be courageous. Challenge orthodoxy. Stand up for what you believe in. When you are in your rocking chair talking to your grandchildren many years from now, be sure you have a good story to tell.",
            "You make a choice: continue living your life feeling muddled in this abyss of self-misunderstanding, or you find your identity independent of it. You draw your own box.",
            "Success is not final, failure is not fatal: it is the courage to continue that counts",
            "You define your own life. Don't let other people write your script.",
            "You are never too old to set another goal or to dream a new dream.",
            "At the end of the day, whether or not those people are comfortable with how you're living your life doesn't matter. What matters is whether you're comfortable with it",
            "People tell you the world looks a certain way. Parents tell you how to think. Schools tell you how to think. TV. Religion. And then at a certain point, if you're lucky, you realize you can make up your own mind. Nobody sets the rules but you. You can design your own life.",
            "Do not allow people to dim your shine because they are blinded. Tell them to put some sunglasses on.",
            "You can be everything. You can be the infinite amount of things that people are",
            "It is during our darkest moments that we must focus to see the light",
    };


    public void initialize(){
                int len=motive.length;
                Random random=new Random();
                int r=random.nextInt(len);
                txtQuote.setText(motive[r]);

                lblPasswordMissMatch.setVisible(false);

                userId=generateId();
                lblUserId.setText(userId);


            }

    public void btnOnActionCreateNewUser(ActionEvent actionEvent) {
        username = txtUserName.getText();
        email = txtEmail.getText();
        password = pwdPassword.getText();
        confirmPassword = pwdConfirmPassword.getText();

        //System.out.println(username);

        if(password.equals(confirmPassword)){
            addUser();

        }
        else {
            lblPasswordMissMatch.setVisible(true);
            new Alert(Alert.AlertType.ERROR,"password mismatch...").showAndWait();

            pwdPassword.clear();
            pwdConfirmPassword.clear();
        }



    }

    public void addUser(){
        Connection connection= DBConnection.getInstance().getConnection();
        try {
            //System.out.println(username);
            PreparedStatement preparedStatement = connection.prepareStatement("insert into user values (?,?,?,?)");
            preparedStatement.setObject(1,userId);
            preparedStatement.setObject(2,username);
            preparedStatement.setObject(3,email);
            preparedStatement.setObject(4,password);

            int i = preparedStatement.executeUpdate();

            if(i!=0){
                new Alert(Alert.AlertType.CONFIRMATION,"user created succesfully...").showAndWait();

                Parent parent=FXMLLoader.load(this.getClass().getResource("../view/LoginForm.fxml"));
                Scene scene=new Scene(parent);
                Stage stage= (Stage) this.rootCreateUser.getScene().getWindow();
                stage.setScene(scene);
                stage.setTitle("To Do List Login...");
                stage.centerOnScreen();
            }
            else{
                new Alert(Alert.AlertType.ERROR,"something went wrong").showAndWait();
            }


        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }




    public String generateId(){
                String setId="";
        Connection connection = DBConnection.getInstance().getConnection();

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select userid from user order by userid desc limit 1");
            boolean isExist = resultSet.next();

            if(isExist){
                String oldId = resultSet.getString(1);
                System.out.println("old id "+oldId);
                String oldIdString = oldId.substring(1, oldId.length());
                System.out.println("oldisstring : "+oldIdString);
                int oldIdInt = Integer.parseInt(oldIdString);
                int newId = oldIdInt+1;
                if(newId<10){
                    setId="U00"+newId;
                }
                else if(newId<100){
                    setId="U0"+newId;
                }
                else{
                    setId="U"+newId;
                }
                System.out.println("set id :"+setId);
            }
            else{
                setId="U001";
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return setId;


            }

}
