package controller;

import db.DBConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;

public class LoginFormController {
    public TextField txtUserName;
    public TextField txtPassword;
    public Label txtQuote;
    public AnchorPane rootLogin;


    static String userid;

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
    }

    public void btnOnActionLogin(ActionEvent actionEvent) throws SQLException {
        String password = txtPassword.getText();
        String userName = txtUserName.getText();


        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("select * from user where name=?");
        preparedStatement.setObject(1,userName);
        ResultSet resultSet = preparedStatement.executeQuery();
        boolean next = resultSet.next();


        if(next){
            userid=resultSet.getString(1);

            String stringPassword = resultSet.getString(4);
            System.out.println(stringPassword);
            if(stringPassword.equals(password)){
                try {
                    Parent parent=FXMLLoader.load(this.getClass().getResource("../view/ToDoForm.fxml"));
                    Scene scene=new Scene(parent);
                    Stage stage= (Stage) this.rootLogin.getScene().getWindow();
                    stage.setScene(scene);
                    stage.setTitle("To Do List");
                    stage.centerOnScreen();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else{
                txtPassword.clear();
                txtPassword.requestFocus();
                new Alert(Alert.AlertType.ERROR,"password doesn't match user name").showAndWait();
            }


        }else{
            txtPassword.clear();
            txtUserName.clear();
            txtUserName.requestFocus();
            new Alert(Alert.AlertType.ERROR,"something went wrong").showAndWait();


        }

    }

    public void btnOnActionCreateNewUser(ActionEvent actionEvent) throws IOException {
     Parent parent=FXMLLoader.load(this.getClass().getResource("../view/CreateUserForm.fxml"));
     Scene scene=new Scene(parent);
     Stage pstage= (Stage) this.rootLogin.getScene().getWindow();
     pstage.setScene(scene);
     pstage.setTitle("create new user...");
     pstage.centerOnScreen();

    }
}
