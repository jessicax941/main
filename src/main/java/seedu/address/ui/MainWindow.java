package seedu.address.ui;

import java.util.logging.Logger;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextInputControl;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import seedu.address.commons.core.GuiSettings;
import seedu.address.commons.core.LogsCenter;
import seedu.address.logic.Logic;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.PanelName;

/**
 * The Main Window. Provides the basic application layout containing
 * a menu bar and space where other JavaFX elements can be placed.
 */
public class MainWindow extends UiPart<Stage> {

    private static final String FXML = "MainWindow.fxml";

    private final Logger logger = LogsCenter.getLogger(getClass());

    private Stage primaryStage;
    private Logic logic;

    // Independent Ui parts residing in this Ui container
    private EntryListPanel entryListPanel;
    private WishListPanel wishListPanel;
    private ResultDisplay resultDisplay;
    private HelpWindow helpWindow;

    @FXML
    private StackPane commandBoxPlaceholder;

    @FXML
    private MenuItem helpMenuItem;

    @FXML
    private StackPane entryListPanelPlaceholder;

    @FXML
    private StackPane resultDisplayPlaceholder;

    @FXML
    private StackPane statusbarPlaceholder;

    @FXML
    private HBox window;

    @FXML
    private VBox sidePanelsPlaceHolder;

    @FXML
    private VBox wishesPlaceHolder;

    @FXML
    private VBox budgetsPlaceHolder;

    @FXML
    private VBox remindersPlaceHolder;

    public MainWindow(Stage primaryStage, Logic logic) {
        super(FXML, primaryStage);

        // Set dependencies
        this.primaryStage = primaryStage;
        this.logic = logic;

        // Configure the UI
        setWindowDefaultSize(logic.getGuiSettings());

        setAccelerators();

        helpWindow = new HelpWindow();

    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    private void setAccelerators() {
        setAccelerator(helpMenuItem, KeyCombination.valueOf("F1"));
    }

    /**
     * Sets the accelerator of a MenuItem.
     * @param keyCombination the KeyCombination value of the accelerator
     */
    private void setAccelerator(MenuItem menuItem, KeyCombination keyCombination) {
        menuItem.setAccelerator(keyCombination);

        /*
         * TODO: the code below can be removed once the bug reported here
         * https://bugs.openjdk.java.net/browse/JDK-8131666
         * is fixed in later version of SDK.
         *
         * According to the bug report, TextInputControl (TextField, TextArea) will
         * consume function-key events. Because CommandBox contains a TextField, and
         * ResultDisplay contains a TextArea, thus some accelerators (e.g F1) will
         * not work when the focus is in them because the key event is consumed by
         * the TextInputControl(s).
         *
         * For now, we add following event filter to capture such key events and open
         * help window purposely so to support accelerators even when focus is
         * in CommandBox or ResultDisplay.
         */
        getRoot().addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getTarget() instanceof TextInputControl && keyCombination.match(event)) {
                menuItem.getOnAction().handle(new ActionEvent());
                event.consume();
            }
        });
    }

    /**
     * Fills up all the placeholders of this window.
     */
    void fillInnerParts() {

        entryListPanel = new EntryListPanel(logic.getFilteredEntryList());
        entryListPanelPlaceholder.getChildren().add(entryListPanel.getRoot());

        resultDisplay = new ResultDisplay();
        resultDisplayPlaceholder.getChildren().add(resultDisplay.getRoot());

        StatusBarFooter statusBarFooter = new StatusBarFooter(logic.getAddressBookFilePath());
        statusbarPlaceholder.getChildren().add(statusBarFooter.getRoot());

        CommandBox commandBox = new CommandBox(this::executeCommand);
        commandBoxPlaceholder.getChildren().add(commandBox.getRoot());

        WishListPanel wishListPanel = new WishListPanel(logic.getFilteredEntryList());
        wishesPlaceHolder.getChildren().add(wishListPanel.getRoot());

        BudgetPanel budgetsPanel = new BudgetPanel(logic.getFilteredEntryList());
        budgetsPlaceHolder.getChildren().add(budgetsPanel.getRoot());

        ReminderPanel reminderPanel = new ReminderPanel(logic.getFilteredEntryList());
        remindersPlaceHolder.getChildren().add(reminderPanel.getRoot());
    }

    /**
     * Sets the default size based on {@code guiSettings}.
     */
    private void setWindowDefaultSize(GuiSettings guiSettings) {
        primaryStage.setHeight(guiSettings.getWindowHeight());
        primaryStage.setWidth(guiSettings.getWindowWidth());
        if (guiSettings.getWindowCoordinates() != null) {
            primaryStage.setX(guiSettings.getWindowCoordinates().getX());
            primaryStage.setY(guiSettings.getWindowCoordinates().getY());
        }
    }

    /**
     * Opens the help window or focuses on it if it's already opened.
     */
    @FXML
    public void handleHelp() {
        if (!helpWindow.isShowing()) {
            helpWindow.show();
        } else {
            helpWindow.focus();
        }
    }

    void show() {
        primaryStage.show();
    }

    /**
     * Closes the application.
     */
    @FXML
    private void handleExit() {
        GuiSettings guiSettings = new GuiSettings(primaryStage.getWidth(), primaryStage.getHeight(),
                (int) primaryStage.getX(), (int) primaryStage.getY());
        logic.setGuiSettings(guiSettings);
        helpWindow.hide();
        primaryStage.hide();
    }

    /**
     * Calls the togglePlaceHolder method with the place holder of the specified panel.
     * @param panelName name of the specified panel to be toggled.
     */
    private void togglePanel(String panelName) {
        switch (panelName) {
        case "wishlist":
            togglePlaceHolder(wishesPlaceHolder);
            break;
        case "budget":
            togglePlaceHolder(budgetsPlaceHolder);
            break;
        case "reminder":
            togglePlaceHolder(remindersPlaceHolder);
            break;
        default:
            break;
        }
    }

    /**
     * Toggles the isVisible and isManaged properties of the specified place holder.
     * @param placeHolder specified place holder to be toggled.
     */
    private void togglePlaceHolder(VBox placeHolder) {
        boolean isManaged = placeHolder.isManaged();
        placeHolder.setManaged(!isManaged);
        boolean isVisible = placeHolder.isVisible();
        placeHolder.setVisible(!isVisible);
    }

    private void togglePlaceHolderForStats() {
        wishesPlaceHolder.setManaged(false);
        wishesPlaceHolder.setVisible(false);
        budgetsPlaceHolder.setManaged(false);
        budgetsPlaceHolder.setVisible(false);
        remindersPlaceHolder.setManaged(false);
        remindersPlaceHolder.setVisible(false);
        sidePanelsPlaceHolder.setManaged(false);
        sidePanelsPlaceHolder.setVisible(false);
    }

    /**
     * Sets both the isVisible and isManaged properties the side panel place holder to false if none of the side panels
     * are visible and managed.
     * Otherwise, both of those properties are set to true.
     */
    private void toggleEntireSidePanelIfNecessary() {
        if (!wishesPlaceHolder.isManaged() && !budgetsPlaceHolder.isManaged() && !remindersPlaceHolder.isManaged()) {
            sidePanelsPlaceHolder.setManaged(false);
            sidePanelsPlaceHolder.setVisible(false);
        } else { // any one of the side panels are managed and visible
            sidePanelsPlaceHolder.setManaged(true);
            sidePanelsPlaceHolder.setVisible(true);
        }
    }

    public EntryListPanel getEntryListPanel() {
        return entryListPanel;
    }

    /**
     * Executes the command and returns the result.
     *
     * @see seedu.address.logic.Logic#execute(String)
     */
    private CommandResult executeCommand(String commandText) throws CommandException,
            ParseException, IllegalArgumentException {
        try {
            CommandResult commandResult = logic.execute(commandText);
            logger.info("Result: " + commandResult.getFeedbackToUser());
            resultDisplay.setFeedbackToUser(commandResult.getFeedbackToUser());

            if (commandResult.isShowHelp()) {
                handleHelp();
            }

            if (commandResult.isExit()) {
                handleExit();
            }

            if (commandResult.isTogglePanel()) {
                PanelName panelName = commandResult.getPanelName();
                String panelNameString = panelName.getName();
                togglePanel(panelNameString);
                toggleEntireSidePanelIfNecessary();
            }

            if (commandResult.isToggleStats()) {
                this.togglePlaceHolderForStats();
                StatisticsWindow stats = new StatisticsWindow(logic);
                entryListPanelPlaceholder.getChildren().clear();
                entryListPanelPlaceholder.getChildren().add(stats.getRoot());
            }

            return commandResult;
        } catch (CommandException | ParseException | IllegalArgumentException e) {
            logger.info("Invalid command: " + commandText);
            resultDisplay.setFeedbackToUser(e.getMessage());
            throw e;
        }
    }
}
