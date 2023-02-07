package controller;

import db.DBConnection;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import tm.ToDoTm;

import java.io.IOException;
import java.sql.*;
import java.util.Optional;

public class ToDoFormContoller {
    public TextField txtNewToDo;
    public ListView<ToDoTm> listToDos;
    public TextArea txtSelected;
    public MenuButton menuCategory;
    public AnchorPane rootToDo;
    public Pane paneAdd;
    public Button btnUpdate;
    public Button btnDelete;

    String menuSelection;
    String id;
    String item;
    String category="daily";
    String userid;

    public void initialize(){
        userid=LoginFormController.userid;

        paneAdd.setVisible(false);
        btnDelete.setDisable(true);
        btnUpdate.setDisable(true);
        txtSelected.setDisable(true);


        listToDos.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<ToDoTm>() {
            @Override
            public void changed(ObservableValue<? extends ToDoTm> observable, ToDoTm oldValue, ToDoTm newValue) {
                btnDelete.setDisable(false);
                btnUpdate.setDisable(false);
                txtSelected.setDisable(false);
                paneAdd.setVisible(false);

                ToDoTm selectedItem = listToDos.getSelectionModel().getSelectedItem();

                if(selectedItem==null){
                    return;
                }

                String desc=selectedItem.getItem();
                txtSelected.setText(desc);
                txtSelected.requestFocus();

                id=selectedItem.getId();

            }
        });
    }



    public void btnOnActionAdd(ActionEvent actionEvent) {
        category=menuSelection;
        System.out.println("cat :"+category);
        item = txtNewToDo.getText();
        idGenerate();


        Connection connection = DBConnection.getInstance().getConnection();

        try {
            PreparedStatement preparedStatement = connection.prepareStatement("insert into dos values (?,?,?,?)");
            preparedStatement.setObject(1,id);
            preparedStatement.setObject(2,item);
            preparedStatement.setObject(3,category);
            preparedStatement.setObject(4,userid);

            int i = preparedStatement.executeUpdate();
            if(i!=0){
                new Alert(Alert.AlertType.INFORMATION,"to do added sucessfully").showAndWait();
                generateList();
            }
            else{
                new Alert(Alert.AlertType.ERROR,"adding to do unccesfull").showAndWait();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        txtNewToDo.clear();
        txtNewToDo.requestFocus();

    }

    public void generateList(){
        category=menuSelection;
        ObservableList<ToDoTm> items = listToDos.getItems();
        items.clear();

        Connection connection = DBConnection.getInstance().getConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("select * from dos where userid=? and category=?");
            preparedStatement.setObject(1,userid);
            preparedStatement.setObject(2,category);

            ResultSet resultSet = preparedStatement.executeQuery();

            while(resultSet.next()){
                String stringid = resultSet.getString(1);
                String stringitem = resultSet.getString(2);
                String stringcategory = resultSet.getString(3);
                String stringuserid = resultSet.getString(4);

                ToDoTm todo=new ToDoTm(stringid,stringitem,stringcategory,stringuserid);
                items.add(todo);
            }
            listToDos.refresh();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void idGenerate(){
        Connection connection = DBConnection.getInstance().getConnection();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select id from dos order by id desc limit 1");
            boolean isExist = resultSet.next();
            if(isExist){
                String oldId = resultSet.getString(1);
                oldId=oldId.substring(1,oldId.length());
                System.out.println("old id of todo :"+oldId);
                int i = Integer.parseInt(oldId);
                i++;
                if(i<10){
                    id="t00"+i;
                }else if(i<100){
                    id="t0"+i;
                }else {
                    id="t"+i;
                }


            }
            else {
                id="t001";
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("output from id generator :"+id);

    }

    public void btnOnActionLogout(ActionEvent actionEvent) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Do you want to logout ? ");
        Optional<ButtonType> buttonType = alert.showAndWait();
        if(buttonType.get().equals(ButtonType.OK)){
            Parent parent= FXMLLoader.load(this.getClass().getResource("../view/LoginForm.fxml"));
            Scene scene=new Scene(parent);

            Stage stage= (Stage) this.rootToDo.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("to do login form");
            stage.centerOnScreen();

        }


    }

    public void btnOnActionUpdate(ActionEvent actionEvent) {
        String text = txtSelected.getText();

        Connection connection = DBConnection.getInstance().getConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("update dos set item=? where userid=? and id=?");
            preparedStatement.setObject(1,text);
            preparedStatement.setObject(2,userid);
            preparedStatement.setObject(3,id);

            int i = preparedStatement.executeUpdate();
            if(i!=0){
                new Alert(Alert.AlertType.CONFIRMATION,"todo updated successfully ").showAndWait();

                generateList();

                txtSelected.clear();
                btnDelete.setDisable(true);
                btnUpdate.setDisable(true);
                txtSelected.setDisable(true);

            }
            else{
                new Alert(Alert.AlertType.ERROR,"fail to update todo").showAndWait();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void btnOnActionDelete(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "do you really want to delete todo ? ");
        Optional<ButtonType> buttonType = alert.showAndWait();
        if(buttonType.get().equals(ButtonType.OK)){
            Connection connection = DBConnection.getInstance().getConnection();
            try {
                PreparedStatement preparedStatement = connection.prepareStatement("delete from dos where userid=? and id=?");
                preparedStatement.setObject(1,userid);
                preparedStatement.setObject(2,id);
                int i = preparedStatement.executeUpdate();
                if (i != 0) {
                    new Alert(Alert.AlertType.CONFIRMATION,"to do deleted succssfully").showAndWait();
                    txtSelected.clear();
                    txtSelected.setDisable(true);
                    btnDelete.setDisable(true);
                    btnUpdate.setDisable(true);

                    generateList();

                }
                else{
                    new Alert(Alert.AlertType.ERROR,"to do deletion unsuccessfull").showAndWait();
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void btnOnActionAddNewToDo(ActionEvent actionEvent) {
        paneAdd.setVisible(true);
        txtNewToDo.requestFocus();


    }


    public void mnuItmDailyOnAction(ActionEvent actionEvent) {
        System.out.println("daily");
        menuCategory.setText("daily");
        menuSelection="daily";

        paneAdd.setVisible(false);
        generateList();
    }

    public void mnuItmProjectsOnAction(ActionEvent actionEvent) {
        System.out.println("projects");
        menuCategory.setText("projects");
        menuSelection="projects";

        paneAdd.setVisible(false);
        generateList();
    }

    public void mnuItmExtraOnAction(ActionEvent actionEvent) {
        System.out.println("Extra");
        menuCategory.setText("Extra");
        menuSelection="Extra";

        paneAdd.setVisible(false);
        generateList();
    }

    public void mnuItmNewOnAction(ActionEvent actionEvent) {
        System.out.println("new");
        menuCategory.setText("new");
        menuSelection="new";

        paneAdd.setVisible(false);
        generateList();
    }



}
