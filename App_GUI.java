import javafx.application.Application;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.geometry.*;
import javafx.stage.Stage;

public class App_GUI extends Application
{
    Stage window;
    Button button, finalButton;
    Scene scene1, scene2;

    DoubleProperty a, b, c, d, z, f, g, h, i, j, k, l;
    int ind = 0, mon = 0, pass = 0;
    double foodSum = 0, lifeSum = 0, transSum = 0;
    ListView <String> listView;

    //Months of the year and the respective number of days in each month
    String months[] = {"January","February","March","April","May","June","July","August","September","October","November","December"};
    int month_days[] = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

    /*Average Operating Margin (OM) factors from 2005 to 2019*/
    double gef[] = {0.5255, 0.5300, 0.5046, 0.4965, 0.4973, 0.5083, 0.5085, 0.4778, 0.4388, 0.4277, 0.4224, 0.4237, 0.4210, 0.4206, 0.4085};

    public static void main (String[] args)
    {
        launch(args);
    }

    @Override
    public void start (Stage primaryStage) throws Exception
    {
        window = primaryStage;
        window.setTitle("EcoTrek");

        GridPane grid = new GridPane();
        grid.setPadding(new Insets (10, 10, 10, 10));
        grid.setVgap(10);
        grid.setHgap(10);

        //Year Label
        Label yearLabel = new Label ("Calendar Year: ");
        grid.add(yearLabel, 0, 1);

        //Year Input
        TextField yearInput = new TextField ("2021");
        grid.add(yearInput, 1, 1);

        //Month Label
        Label monthLabel = new Label ("Month Number: ");
        grid.add(monthLabel, 0, 2);

        //Month Input
        TextField monthInput = new TextField();
        monthInput.setPromptText("1");
        grid.add(monthInput, 1, 2);

        //Label for family members
        Label memLabel = new Label ("Number of household members: ");
        grid.add(memLabel, 0, 3);

        //Input for number of family members
        TextField memInput = new TextField ();
        memInput.setPromptText("4");
        grid.add(memInput, 1, 3);

        Label note_Label = new Label("Please refer to your electricity and water bills for the entered month of the calendar year.");
        Label elc_Label = new Label("Total amount of electricity consumed BY THE HOUSEHOLD (in kWh) for the month of the calendar year: ");
        Label water_Label = new Label("Total quantity of water consumed BY THE HOUSEHOLD (in Cu M) for the month of the calendar year: ");
        Label waste_Label = new Label("Approximate quantity of domestic waste generated BY THE HOUSEHOLD (in kg) for the month of the calendar year: ");

        Label elc_factor = new Label();
        Label water_factor = new Label();
        Label waste_factor = new Label();

        water_factor.setText("x 1.3000 kgCO2/CuM");
        waste_factor.setText("x 1.6700 kgCO2/kg");

        Label car_factor = new Label();
        Label bus_factor = new Label();
        Label mrt_factor = new Label();

        car_factor.setText("x 0.1400 kgCO2/vehicle-km");
        bus_factor.setText("x 0.0190 kgCO2/passenger-km");
        mrt_factor.setText("x 0.0130 kgCO2/passenger-km");

        button = new Button("Submit");
        grid.add(button, 1, 5);

        button.setOnAction(e ->
        {
            if ((isInt(yearInput))&&(isInt(monthInput))&&(isInt(memInput)))
            {
                if ((Integer.parseInt(monthInput.getText())<1)||(Integer.parseInt(monthInput.getText())>12))
                    System.out.println("MONTH NUMBER IS OUT OF RANGE!");

                if ((Integer.parseInt(yearInput.getText())<2005)||(Integer.parseInt(yearInput.getText())>2021))
                    System.out.println("SORRY! NO DATA IS AVAILABLE FOR " + yearInput.getText().trim() + ".");

                if ((Integer.parseInt(monthInput.getText())>=1)&&(Integer.parseInt(monthInput.getText())<=12)&&
                        (Integer.parseInt(yearInput.getText())>=2005)&&(Integer.parseInt(yearInput.getText())<=2021))
                {
                    mon = Integer.parseInt(monthInput.getText()) - 1;

                    if ((Integer.parseInt(yearInput.getText())>=2005)&&(Integer.parseInt(yearInput.getText())<=2019))
                        ind = Integer.parseInt(yearInput.getText()) - 2005;
                    else
                        ind = 14;

                    if (((Integer.parseInt(yearInput.getText())%4==0)&&(Integer.parseInt(yearInput.getText())%100!=0))||(Integer.parseInt(yearInput.getText())%400==0)) //checking if it is a leap year
                        month_days[1] = 29;

                    //Editing text labels
                    note_Label.setText("Please refer to your electricity and water bills for " + months[mon] + " " + yearInput.getText() + ".");
                    elc_Label.setText("Total amount of electricity consumed BY THE HOUSEHOLD (in kWh) for " + months[mon] + " " + yearInput.getText() + ": ");
                    water_Label.setText("Total quantity of water consumed BY THE HOUSEHOLD (in Cu M) for " + months[mon] + " " + yearInput.getText() + ": ");
                    waste_Label.setText("Approximate quantity of domestic waste generated BY THE HOUSEHOLD (in kg) for " + months[mon] + " " + yearInput.getText() + ": ");

                    elc_factor.setText("x " + gef[ind] + " kgCO2/MWh");

                    window.setScene(scene2);
                }
            }

            if (!(isInt(yearInput)))
                System.out.println("INVALID ENTRY OF CALENDAR YEAR!");

            if (!isInt(monthInput))
                System.out.println("INVALID ENTRY OF MONTH NUMBER!");

            if (!isInt(memInput))
                System.out.println("INVALID ENTRY OF THE NUMBER OF HOUSEHOLD MEMBERS!");

            System.out.println();
        });

        grid.setAlignment(Pos.CENTER);
        scene1 = new Scene(grid, 400, 250);

        GridPane gridA = new GridPane();
        gridA.setPadding(new Insets (20, 20, 20, 20));
        gridA.setVgap(20);
        gridA.setHgap(20);

        note_Label.setStyle("-fx-font: 14 arial;");
        gridA.add(note_Label, 0, 0);

        Label inputs = new Label("INPUTS");
        inputs.setStyle("-fx-font: 14 arial;");
        gridA.add(inputs, 1, 0);

        Label results = new Label("RESULTS");
        results.setStyle("-fx-font: 14 arial;");
        gridA.add(results, 4, 0);

        elc_Label.setStyle("-fx-font: 14 arial;");
        gridA.add(elc_Label, 0, 1);

        //Electricity Input
        TextField elc_Input = new TextField();
        elc_Input.setStyle("-fx-font: 14 arial;");
        gridA.add(elc_Input, 1, 1);

        elc_factor.setStyle("-fx-font: 14 arial;");
        gridA.add(elc_factor, 2, 1);

        Label elc_ems = new Label("Emissions from Electricity Consumption: ");
        elc_ems.setStyle("-fx-font: 14 arial;");
        gridA.add(elc_ems, 4, 1);

        Label elc_emissions = new Label("0");
        elc_emissions.setStyle("-fx-font: 14 arial;");
        gridA.add(elc_emissions, 5, 1);

        Label unit = new Label("kgCO2/day/person");
        unit.setStyle("-fx-font: 14 arial;");
        gridA.add(unit, 6, 1);

        water_Label.setStyle("-fx-font: 14 arial;");
        gridA.add(water_Label, 0, 2);

        //Water Input
        TextField water_Input = new TextField();
        water_Input.setStyle("-fx-font: 14 arial;");
        gridA.add(water_Input, 1, 2);

        water_factor.setStyle("-fx-font: 14 arial;");
        gridA.add(water_factor, 2, 2);

        Label water_ems = new Label("Emissions from Water Consumption: ");
        water_ems.setStyle("-fx-font: 14 arial;");
        gridA.add(water_ems, 4, 2);

        Label water_emissions = new Label("0");
        water_emissions.setStyle("-fx-font: 14 arial;");
        gridA.add(water_emissions, 5, 2);

        Label unit1 = new Label("kgCO2/day/person");
        unit1.setStyle("-fx-font: 14 arial;");
        gridA.add(unit1, 6, 2);

        waste_Label.setStyle("-fx-font: 14 arial;");
        gridA.add(waste_Label, 0, 3);

        //Waste Input
        TextField waste_Input = new TextField();
        waste_Input.setStyle("-fx-font: 14 arial;");
        gridA.add(waste_Input, 1, 3);

        waste_factor.setStyle("-fx-font: 14 arial;");
        gridA.add(waste_factor, 2, 3);

        Label waste_ems = new Label("Emissions from Waste Generation: ");
        waste_ems.setStyle("-fx-font: 14 arial;");
        gridA.add(waste_ems, 4, 3);

        Label waste_emissions = new Label("0");
        waste_emissions.setStyle("-fx-font: 14 arial;");
        gridA.add(waste_emissions, 5, 3);

        Label unit2 = new Label("kgCO2/day/person");
        unit2.setStyle("-fx-font: 14 arial;");
        gridA.add(unit2, 6, 3);

        Label lifeLabel = new Label("Emissions due to Lifestyle Habits: ");
        lifeLabel.setStyle("-fx-font: 16 arial;");
        gridA.add(lifeLabel, 4, 4);

        Label lifeOutput = new Label("0");
        lifeOutput.setStyle("-fx-font: 16 arial;");
        gridA.add(lifeOutput, 5, 4);

        Label lifeUnit = new Label("kgCO2/day/person");
        lifeUnit.setStyle("-fx-font: 16 arial;");
        gridA.add(lifeUnit, 6, 4);

        //Transport Label
        Label trans_Label = new Label("DAILY distance (in km) travelled by: ");
        trans_Label.setStyle("-fx-font: 14 arial;");
        gridA.add(trans_Label, 0, 5);

        Label dist_Label = new Label("DISTANCE TRAVELLED (km)");
        dist_Label.setStyle("-fx-font: 14 arial;");
        gridA.add(dist_Label, 1, 5);

        //Car Label
        Label car_Label = new Label("Car: ");
        car_Label.setStyle("-fx-font: 14 arial;");
        HBox hBox1 = new HBox(10);
        hBox1.getChildren().add(car_Label);

        Label pass_Label = new Label("Number of passengers: ");
        pass_Label.setStyle("-fx-font: 14 arial;");
        TextField pass_Input = new TextField();
        pass_Input.setStyle("-fx-font: 14 arial;");

        HBox hBox2 = new HBox (10);
        hBox2.getChildren().addAll(pass_Label, pass_Input);

        HBox hBox3 = new HBox (20);
        hBox3.getChildren().addAll(hBox1, hBox2);
        gridA.add(hBox3, 0, 6);

        //Car Input
        TextField car_Input = new TextField();
        car_Input.setStyle("-fx-font: 14 arial;");
        gridA.add(car_Input, 1, 6);

        car_factor.setStyle("-fx-font: 14 arial;");
        gridA.add(car_factor, 2, 6);

        Label car_ems = new Label("Car Emissions: ");
        car_ems.setStyle("-fx-font: 14 arial;");
        gridA.add(car_ems, 4, 6);

        Label car_emissions = new Label("0");
        car_emissions.setStyle("-fx-font: 14 arial;");
        gridA.add(car_emissions, 5, 6);

        Label unit3 = new Label("kgCO2/day/person");
        unit3.setStyle("-fx-font: 14 arial;");
        gridA.add(unit3, 6, 6);

        //Bus Label
        Label bus_Label = new Label("Bus: ");
        bus_Label.setStyle("-fx-font: 14 arial;");
        gridA.add(bus_Label, 0, 7);

        //Bus Input
        TextField bus_Input = new TextField();
        bus_Input.setStyle("-fx-font: 14 arial;");
        gridA.add(bus_Input, 1, 7);

        bus_factor.setStyle("-fx-font: 14 arial;");
        gridA.add(bus_factor, 2, 7);

        Label bus_ems = new Label("Bus Emissions: ");
        bus_ems.setStyle("-fx-font: 14 arial;");
        gridA.add(bus_ems, 4, 7);

        Label bus_emissions = new Label("0");
        bus_emissions.setStyle("-fx-font: 14 arial;");
        gridA.add(bus_emissions, 5, 7);

        Label unit4 = new Label("kgCO2/day/person");
        unit4.setStyle("-fx-font: 14 arial;");
        gridA.add(unit4, 6, 7);

        //MRT Label
        Label mrt_Label = new Label("MRT: ");
        mrt_Label.setStyle("-fx-font: 14 arial;");
        gridA.add(mrt_Label, 0, 8);

        //MRT Input
        TextField mrt_Input = new TextField();
        mrt_Input.setStyle("-fx-font: 14 arial;");
        gridA.add(mrt_Input, 1, 8);

        mrt_factor.setStyle("-fx-font: 14 arial;");
        gridA.add(mrt_factor, 2, 8);

        Label mrt_ems = new Label("MRT Emissions: ");
        mrt_ems.setStyle("-fx-font: 14 arial;");
        gridA.add(mrt_ems, 4, 8);

        Label mrt_emissions = new Label("0");
        mrt_emissions.setStyle("-fx-font: 14 arial;");
        gridA.add(mrt_emissions, 5, 8);

        Label unit5 = new Label("kgCO2/day/person");
        unit5.setStyle("-fx-font: 14 arial;");
        gridA.add(unit5, 6, 8);

        Label transLabel = new Label("Gross Transport Emissions: ");
        transLabel.setStyle("-fx-font: 16 arial;");
        gridA.add(transLabel, 4, 9);

        Label transOutput = new Label("0");
        transOutput.setStyle("-fx-font: 16 arial;");
        gridA.add(transOutput, 5, 9);

        Label transUnit = new Label("kgCO2/day/person");
        transUnit.setStyle("-fx-font: 16 arial;");
        gridA.add(transUnit, 6, 9);

        Label foodItems = new Label("Please select the food items that you consumed today from the list below: ");
        foodItems.setStyle("-fx-font: 14 arial;");
        gridA.add(foodItems, 0, 10);

        listView = new ListView<>();
        listView.getItems().addAll(("Chicken (Fresh)\n" + "- " + String.format("%.4f", 3.380368) + " kgCO2/kg"),
                ("Chicken (Frozen)\n" + "- " + String.format("%.4f", ((3.763021 + 3.63258) / 2)) + " kgCO2/kg"),
                ("Duck (Fresh)\n" + "- " + String.format("%.4f", 4.142192) + " kgCO2/kg"), ("Duck (Frozen)\n" + "- " + String.format("%.4f", 4.250369) + " kgCO2/kg"),
                ("Mutton (Chilled)\n" + "- " + String.format("%.4f", 21.2201) + " kgCO2/kg"), ("Mutton (Frozen)\n" + "- " + String.format("%.4f", 14.34296) + " kgCO2/kg"),
                ("Pork (Fresh)\n" + "- " + String.format("%.4f", 9.00934731) + " kgCO2/kg"),
                ("Pork (Chilled)\n" + "- " + String.format("%.4f", ((26.9740328 + 18.3298817) / 2)) + " kgCO2/kg"),
                ("Pork (Frozen)\n" + "- " + String.format("%.4f", ((8.56600375 + 11.4827046 + 9.81102315 + 9.36335679) / 4)) + " kgCO2/kg"),
                ("Beef (Chilled)\n" + "- " + String.format("%.4f", ((38.38723 + 27.49682 + 29.29078) / 3)) + " kgCO2/kg"),
                ("Beef (Frozen)\n" + "- " + String.format("%.4f", ((19.9699 + 20.61967 + 19.64468) / 3)) + " kgCO2/kg"),
                ("Eggs (Fresh)\n" + "- " + String.format("%.4f", ((3.10849 + 2.955663) / 2)) + " kgCO2/kg"),
                ("Banana\n" + "- " + String.format("%.4f", ((0.382615 + 0.390919) / 2)) + " kgCO2/kg"), ("Watermelon\n" + "- " + String.format("%.4f", 0.318382) + " kgCO2/kg"),
                ("Papaya\n" + "- " + String.format("%.4f", 0.335058) + " kgCO2/kg"), ("Pineapple\n" + "- " + String.format("%.4f", 0.28408) + " kgCO2/kg"),
                ("Orange\n" + "- " + String.format("%.4f", ((0.50028 + 0.996865 + 0.593861 + 0.566273) / 4)) + " kgCO2/kg"),
                ("Tomato\n" + "- " + String.format("%.4f", 0.598867) + " kgCO2/kg"),
                ("Cabbage\n" + "- " + String.format("%.4f", ((0.741585 + 0.753856) / 2)) + " kgCO2/kg"),
                ("Carrot\n" + "- " + String.format("%.4f", ((1.318346 + 1.173404 + 1.063548) / 3)) + " kgCO2/kg"),
                ("Beansprout\n" + "- " + String.format("%.4f", 0.245715) + " kgCO2/kg"),
                ("Onion\n" + "- " + String.format("%.4f", ((0.990663 + 0.843198 + 1.186481 + 1.119623) / 4)) + " kgCO2/kg"),
                ("Potato\n" + "- " + String.format("%.4f", ((0.414978 + 0.396377 + 0.371213 + 0.483097 + 1.096731) / 5)) + " kgCO2/kg"),
                ("Wheat\n" + "- " + String.format("%.4f", ((0.609043 + 0.923174) / 2)) + " kgCO2/kg"),
                ("Rice\n" + "- " + String.format("%.4f", ((2.533341 + 2.671638 + 2.518485) / 3)) + " kgCO2/kg"),
                ("Chinese Cabbage\n" + "- " + String.format("%.4f", ((0.429858 + 0.43304 + 0.342679) / 3)) + " kgCO2/kg"),
                ("Spinach\n" + "- " + String.format("%.4f", ((0.273495 + 0.18902) / 2)) + " kgCO2/kg"),
                ("Lettuce\n" + "- " + String.format("%.4f", ((0.469264 + 0.445305 + 0.375591 + 0.422315 + 0.26783 + 1.537362) / 6)) + " kgCO2/kg"),
                ("Catfish\n" + "- " + String.format("%.4f", 5.684848) + " kgCO2/kg"), ("Salmon (Chilled)\n" + "- " + String.format("%.4f", 13.57337) + " kgCO2/kg"),
                ("Salmon (Frozen)\n" + "- " + String.format("%.4f", ((2.294267 + 2.25376) / 2)) + " kgCO2/kg"),
                ("Mackerel\n" + "- " + String.format("%.4f", ((4.685541 + 5.557604 + 5.171631) / 3)) + " kgCO2/kg"),
                ("Aquaculture\n" + "- " + String.format("%.4f", 3.73018) + " kgCO2/kg"), ("Capture Fishing\n" + "- " + String.format("%.4f", 4.262754) + " kgCO2/kg"),
                ("Shrimp (Frozen)\n" + "- " + String.format("%.4f", ((5.82830545 + 5.90167102 + 6.48815366 + 7.22324152 + 5.13862442) / 5)) + " kgCO2/kg"),
                ("Crab (Frozen)\n" + "- " + String.format("%.4f", ((4.86431293 + 4.96840779 + 5.04473111) / 3)) + " kgCO2/kg"),
                ("Crab (Fresh)\n" + "- " + String.format("%.4f", ((4.79812762 + 4.84477302 + 4.92228905) / 3)) + " kgCO2/kg"),
                ("Squid (Fresh)\n" + "- " + String.format("%.4f", ((4.76347232 + 4.73865097 + 4.90059262) / 3)) + " kgCO2/kg"),
                ("Squid (Frozen)\n" + "- " + String.format("%.4f", ((4.94249148 + 4.91439978 + 5.07338584) / 3)) + " kgCO2/kg"));

        listView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        listView.setStyle("-fx-font: 14 arial;");
        gridA.add(listView, 0, 11);

        Label quantityLabel = new Label("Average Quantity (in kg) of each food item consumed: ");
        quantityLabel.setStyle("-fx-font: 14 arial;");

        TextField quantityInput = new TextField("0.25");
        quantityInput.setStyle("-fx-font: 14 arial;");

        HBox hBox = new HBox (10);
        hBox.getChildren().addAll(quantityLabel, quantityInput);
        gridA.add(hBox, 0, 12);

        Label foodLabel = new Label("Emissions from Food Consumption: ");
        foodLabel.setStyle("-fx-font: 16 arial;");
        gridA.add(foodLabel, 4, 11);

        Label foodOutput = new Label("0");
        foodOutput.setStyle("-fx-font: 16 arial;");
        gridA.add(foodOutput, 5, 11);

        Label unit6 = new Label("kgCO2/day/person");
        unit6.setStyle("-fx-font: 16 arial;");
        gridA.add(unit6, 6, 11);

        finalButton = new Button("Check your Footprint!");
        finalButton.setStyle("-fx-font: 16 arial;");
        gridA.add(finalButton, 2, 13);

        finalButton.setOnAction(e ->
        {
            if (isPositiveDouble(elc_Input)) {
                a = new SimpleDoubleProperty(Double.parseDouble(elc_Input.getText()));
                b = new SimpleDoubleProperty();
                b.bind(a.multiply(gef[ind]).divide(Integer.parseInt(memInput.getText()) * month_days[mon] * 1000));
                String str_b = String.format("%.4f", b.get());
                elc_emissions.setText(str_b);
                lifeSum = lifeSum + b.get();
            }
            else
                elc_emissions.setText("0");

            if (isPositiveDouble(water_Input)) {
                c = new SimpleDoubleProperty(Double.parseDouble(water_Input.getText()));
                d = new SimpleDoubleProperty();
                d.bind(c.multiply(1.30).divide(Integer.parseInt(memInput.getText()) * month_days[mon]));
                String str_d = String.format("%.4f", d.get());
                water_emissions.setText(str_d);
                lifeSum = lifeSum + d.get();
            }
            else
                water_emissions.setText("0");

            if (isPositiveDouble(waste_Input)) {
                z = new SimpleDoubleProperty(Double.parseDouble(waste_Input.getText()));
                f = new SimpleDoubleProperty();
                f.bind(z.multiply(1.67).divide(Integer.parseInt(memInput.getText()) * month_days[mon]));
                String str_f = String.format("%.4f", f.get());
                waste_emissions.setText(str_f);
                lifeSum = lifeSum + f.get();
            }
            else
                waste_emissions.setText("0");

            String strLifeSum = String.format("%.4f", lifeSum);
            lifeOutput.setText(strLifeSum);
            if (lifeSum==0)
            {
                elc_emissions.setText("0");
                water_emissions.setText("0");
                waste_emissions.setText("0");

            }
            lifeSum = 0;

            if ((isPositiveDouble(car_Input)) && (isInt(pass_Input))) {
                g = new SimpleDoubleProperty(Double.parseDouble(car_Input.getText()));
                pass = Integer.parseInt(pass_Input.getText());
                h = new SimpleDoubleProperty();
                h.bind(g.multiply(0.14).divide(pass));
                String str_h = String.format("%.4f", h.get());
                car_emissions.setText(str_h);
                transSum = transSum + h.get();
            }
            else
                car_emissions.setText("0");

            if (isPositiveDouble(bus_Input)) {
                i = new SimpleDoubleProperty(Double.parseDouble(bus_Input.getText()));
                j = new SimpleDoubleProperty();
                j.bind(i.multiply(0.019));
                String str_j = String.format("%.4f", j.get());
                bus_emissions.setText(str_j);
                transSum = transSum + j.get();
            }
            else
                bus_emissions.setText("0");

            if (isPositiveDouble(mrt_Input)) {
                k = new SimpleDoubleProperty(Double.parseDouble(mrt_Input.getText()));
                l = new SimpleDoubleProperty();
                l.bind(k.multiply(0.013));
                String str_l = String.format("%.4f", l.get());
                mrt_emissions.setText(str_l);
                transSum = transSum + l.get();
            }
            else
                mrt_emissions.setText("0");

            String strTransSum = String.format("%.4f", transSum);
            transOutput.setText(strTransSum);
            if (transSum==0)
            {
                car_emissions.setText("0");
                bus_emissions.setText("0");
                mrt_emissions.setText("0");
            }
            transSum = 0;

            ObservableList<String> food_items;
            food_items = listView.getSelectionModel().getSelectedItems();

            if (isPositiveDouble(quantityInput)) {
                for (String f : food_items) {
                    if (f.equals("Chicken (Fresh)\n" + "- " + String.format("%.4f", 3.380368) + " kgCO2/kg"))
                        foodSum = foodSum + 3.380368;

                    if (f.equals("Chicken (Frozen)\n" + "- " + String.format("%.4f", ((3.763021 + 3.63258) / 2)) + " kgCO2/kg"))
                        foodSum = foodSum + ((3.763021 + 3.63258) / 2);

                    if (f.equals("Duck (Fresh)\n" + "- " + String.format("%.4f", 4.142192) + " kgCO2/kg"))
                        foodSum = foodSum + 4.142192;

                    if (f.equals("Duck (Frozen)\n" + "- " + String.format("%.4f", 4.250369) + " kgCO2/kg"))
                        foodSum = foodSum + 4.250369;

                    if (f.equals("Mutton (Chilled)\n" + "- " + String.format("%.4f", 21.2201) + " kgCO2/kg"))
                        foodSum = foodSum + 21.2201;

                    if (f.equals("Mutton (Frozen)\n" + "- " + String.format("%.4f", 14.34296) + " kgCO2/kg"))
                        foodSum = foodSum + 14.34296;

                    if (f.equals("Pork (Fresh)\n" + "- " + String.format("%.4f", 9.00934731) + " kgCO2/kg"))
                        foodSum = foodSum + 9.00934731;

                    if (f.equals("Pork (Chilled)\n" + "- " + String.format("%.4f", ((26.9740328 + 18.3298817) / 2)) + " kgCO2/kg"))
                        foodSum = foodSum + ((26.9740328 + 18.3298817) / 2);

                    if (f.equals("Pork (Frozen)\n" + "- " + String.format("%.4f", ((8.56600375 + 11.4827046 + 9.81102315 + 9.36335679) / 4)) + " kgCO2/kg"))
                        foodSum = foodSum + ((8.56600375 + 11.4827046 + 9.81102315 + 9.36335679) / 4);

                    if (f.equals("Beef (Chilled)\n" + "- " + String.format("%.4f", ((38.38723 + 27.49682 + 29.29078) / 3)) + " kgCO2/kg"))
                        foodSum = foodSum + ((38.38723 + 27.49682 + 29.29078) / 3);

                    if (f.equals("Beef (Frozen)\n" + "- " + String.format("%.4f", ((19.9699 + 20.61967 + 19.64468) / 3)) + " kgCO2/kg"))
                        foodSum = foodSum + ((19.9699 + 20.61967 + 19.64468) / 3);

                    if (f.equals("Eggs (Fresh)\n" + "- " + String.format("%.4f", ((3.10849 + 2.955663) / 2)) + " kgCO2/kg"))
                        foodSum = foodSum + ((3.10849 + 2.955663) / 2);

                    if (f.equals("Banana\n" + "- " + String.format("%.4f", ((0.382615 + 0.390919) / 2)) + " kgCO2/kg"))
                        foodSum = foodSum + ((0.382615 + 0.390919) / 2);

                    if (f.equals("Watermelon\n" + "- " + String.format("%.4f", 0.318382) + " kgCO2/kg"))
                        foodSum = foodSum + 0.318382;

                    if (f.equals("Papaya\n" + "- " + String.format("%.4f", 0.335058) + " kgCO2/kg"))
                        foodSum = foodSum + 0.335058;

                    if (f.equals("Pineapple\n" + "- " + String.format("%.4f", 0.28408) + " kgCO2/kg"))
                        foodSum = foodSum + 0.28408;

                    if (f.equals("Orange\n" + "- " + String.format("%.4f", ((0.50028 + 0.996865 + 0.593861 + 0.566273) / 4)) + " kgCO2/kg"))
                        foodSum = foodSum + ((0.50028 + 0.996865 + 0.593861 + 0.566273) / 4);

                    if (f.equals("Tomato\n" + "- " + String.format("%.4f", 0.598867) + " kgCO2/kg"))
                        foodSum = foodSum + 0.598867;

                    if (f.equals("Cabbage\n" + "- " + String.format("%.4f", ((0.741585 + 0.753856) / 2)) + " kgCO2/kg"))
                        foodSum = foodSum + ((0.741585 + 0.753856) / 2);

                    if (f.equals("Carrot\n" + "- " + String.format("%.4f", ((1.318346 + 1.173404 + 1.063548) / 3)) + " kgCO2/kg"))
                        foodSum = foodSum + ((1.318346 + 1.173404 + 1.063548) / 3);

                    if (f.equals("Beansprout\n" + "- " + String.format("%.4f", 0.245715) + " kgCO2/kg"))
                        foodSum = foodSum + 0.245715;

                    if (f.equals("Onion\n" + "- " + String.format("%.4f", ((0.990663 + 0.843198 + 1.186481 + 1.119623) / 4)) + " kgCO2/kg"))
                        foodSum = foodSum + ((0.990663 + 0.843198 + 1.186481 + 1.119623) / 4);

                    if (f.equals("Potato\n" + "- " + String.format("%.4f", ((0.414978 + 0.396377 + 0.371213 + 0.483097 + 1.096731) / 5)) + " kgCO2/kg"))
                        foodSum = foodSum + ((0.414978 + 0.396377 + 0.371213 + 0.483097 + 1.096731) / 5);

                    if (f.equals("Wheat\n" + "- " + String.format("%.4f", ((0.609043 + 0.923174) / 2)) + " kgCO2/kg"))
                        foodSum = foodSum + ((0.609043 + 0.923174) / 2);

                    if (f.equals("Rice\n" + "- " + String.format("%.4f", ((2.533341 + 2.671638 + 2.518485) / 3)) + " kgCO2/kg"))
                        foodSum = foodSum + ((2.533341 + 2.671638 + 2.518485) / 3);

                    if (f.equals("Chinese Cabbage\n" + "- " + String.format("%.4f", ((0.429858 + 0.43304 + 0.342679) / 3)) + " kgCO2/kg"))
                        foodSum = foodSum + ((0.429858 + 0.43304 + 0.342679) / 3);

                    if (f.equals("Spinach\n" + "- " + String.format("%.4f", ((0.273495 + 0.18902) / 2)) + " kgCO2/kg"))
                        foodSum = foodSum + ((0.273495 + 0.18902) / 2);

                    if (f.equals("Lettuce\n" + "- " + String.format("%.4f", ((0.469264 + 0.445305 + 0.375591 + 0.422315 + 0.26783 + 1.537362) / 6)) + " kgCO2/kg"))
                        foodSum = foodSum + ((0.469264 + 0.445305 + 0.375591 + 0.422315 + 0.26783 + 1.537362) / 6);

                    if (f.equals("Catfish\n" + "- " + String.format("%.4f", 5.684848) + " kgCO2/kg"))
                        foodSum = foodSum + 5.684848;

                    if (f.equals("Salmon (Chilled)\n" + "- " + String.format("%.4f", 13.57337) + " kgCO2/kg"))
                        foodSum = foodSum + 13.57337;

                    if (f.equals("Salmon (Frozen)\n" + "- " + String.format("%.4f", ((2.294267 + 2.25376) / 2)) + " kgCO2/kg"))
                        foodSum = foodSum + ((2.294267 + 2.25376) / 2);

                    if (f.equals("Mackerel\n" + "- " + String.format("%.4f", ((4.685541 + 5.557604 + 5.171631) / 3)) + " kgCO2/kg"))
                        foodSum = foodSum + ((4.685541 + 5.557604 + 5.171631) / 3);

                    if (f.equals("Aquaculture\n" + "- " + String.format("%.4f", 3.73018) + " kgCO2/kg"))
                        foodSum = foodSum + 3.73018;

                    if (f.equals("Capture Fishing\n" + "- " + String.format("%.4f", 4.262754) + " kgCO2/kg"))
                        foodSum = foodSum + 4.262754;

                    if (f.equals("Shrimp (Frozen)\n" + "- " + String.format("%.4f", ((5.82830545 + 5.90167102 + 6.48815366 + 7.22324152 + 5.13862442) / 5)) + " kgCO2/kg"))
                        foodSum = foodSum + ((5.82830545 + 5.90167102 + 6.48815366 + 7.22324152 + 5.13862442) / 5);

                    if (f.equals("Crab (Frozen)\n" + "- " + String.format("%.4f", ((4.86431293 + 4.96840779 + 5.04473111) / 3)) + " kgCO2/kg"))
                        foodSum = foodSum + ((4.86431293 + 4.96840779 + 5.04473111) / 3);

                    if (f.equals("Crab (Fresh)\n" + "- " + String.format("%.4f", ((4.79812762 + 4.84477302 + 4.92228905) / 3)) + " kgCO2/kg"))
                        foodSum = foodSum + ((4.79812762 + 4.84477302 + 4.92228905) / 3);

                    if (f.equals("Squid (Fresh)\n" + "- " + String.format("%.4f", ((4.76347232 + 4.73865097 + 4.90059262) / 3)) + " kgCO2/kg"))
                        foodSum = foodSum + ((4.76347232 + 4.73865097 + 4.90059262) / 3);

                    if (f.equals("Squid (Frozen)\n" + "- " + String.format("%.4f", ((4.94249148 + 4.91439978 + 5.07338584) / 3)) + " kgCO2/kg"))
                        foodSum = foodSum + ((4.94249148 + 4.91439978 + 5.07338584) / 3);
                }
                foodSum = foodSum * Double.parseDouble(quantityInput.getText());
            }

            String strFoodSum = String.format("%.4f", foodSum);
            foodOutput.setText(strFoodSum);
            foodSum = 0;
        });

        scene2 = new Scene (gridA, 1800, 1000);

        window.setScene(scene1);
        window.show();
    }

    private boolean isInt (TextField input)
    {
        try
        {
            int num = Integer.parseInt(input.getText());
            return (num>0);
        }
        catch (NumberFormatException e)
        {
            return false;
        }
    }

    private boolean isPositiveDouble (TextField input)
    {
        try
        {
            double dub = Double.parseDouble(input.getText());
            return (dub>0);
        }
        catch (NumberFormatException e)
        {
            return false;
        }
    }
}