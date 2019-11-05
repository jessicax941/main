package seedu.guilttrip.model;

import java.nio.file.Path;
import java.util.List;
import java.util.function.Predicate;

import javafx.beans.property.DoubleProperty;
import javafx.collections.ObservableList;

import seedu.guilttrip.commons.core.GuiSettings;
import seedu.guilttrip.model.entry.AutoExpense;
import seedu.guilttrip.model.entry.Budget;
import seedu.guilttrip.model.entry.Category;
import seedu.guilttrip.model.entry.CategoryList;
import seedu.guilttrip.model.entry.Date;
import seedu.guilttrip.model.entry.Entry;
import seedu.guilttrip.model.entry.Expense;
import seedu.guilttrip.model.entry.Income;
import seedu.guilttrip.model.entry.SortSequence;
import seedu.guilttrip.model.entry.SortType;
import seedu.guilttrip.model.entry.Wish;
import seedu.guilttrip.model.reminders.Reminder;
import seedu.guilttrip.model.reminders.conditions.Condition;
import seedu.guilttrip.model.statistics.CategoryStatistics;
import seedu.guilttrip.model.statistics.DailyStatistics;

/**
 * The API of the Model component.
 */
public interface Model {
    /** {@code Predicate} that always evaluate to true */
    Predicate<Entry> PREDICATE_SHOW_ALL_ENTRIES = unused -> true;
    Predicate<Expense> PREDICATE_SHOW_ALL_EXPENSES = unused -> true;
    Predicate<Income> PREDICATE_SHOW_ALL_INCOMES = unused -> true;
    Predicate<Wish> PREDICATE_SHOW_ALL_WISHES = unused -> true;
    Predicate<Budget> PREDICATE_SHOW_ALL_BUDGETS = unused -> true;
    Predicate<AutoExpense> PREDICATE_SHOW_ALL_AUTOEXPENSES = unused -> true;
    Predicate<Condition> PREDICATE_SHOW_ALL_CONDITIONS = unused -> true;
    Predicate<Reminder> PREDICATE_SHOW_ACTIVE_REMINDERS =
        x -> !x.getStatus().equals(Reminder.Status.unmet);
    Predicate<Reminder> PREDICATE_SHOW_ALL_REMINDERS = unused -> true;

    DoubleProperty getTotalExpenseForPeriod();

    DoubleProperty getTotalIncomeForPeriod();

    ObservableList<DailyStatistics> getListOfStatsForBarChart();

    ObservableList<CategoryStatistics> getListOfStatsForExpense();

    ObservableList<CategoryStatistics> getListOfStatsForIncome();

    void updateListOfStats();

    void updateListOfStats(List<Date> period);

    void updateBarCharts();

    void updateBarCharts(Date month);

    /**
     * Replaces user prefs data with the data in {@code userPrefs}.
     */
    void setUserPrefs(ReadOnlyUserPrefs userPrefs);

    /**
     * Returns the user prefs.
     */
    ReadOnlyUserPrefs getUserPrefs();

    /**
     * Returns the user prefs' GUI settings.
     */
    GuiSettings getGuiSettings();

    /**
     * Sets the user prefs' GUI settings.
     */
    void setGuiSettings(GuiSettings guiSettings);

    /**
     * Returns the user prefs' guilttrip book file path.
     */
    Path getAddressBookFilePath();

    /**
     * Sets the user prefs' guilttrip book file path.
     */
    void setAddressBookFilePath(Path addressBookFilePath);

    /**
     * Replaces guilttrip book data with the data in {@code addressBook}.
     */
    void setAddressBook(ReadOnlyGuiltTrip addressBook);

    /** Returns the GuiltTrip */
    ReadOnlyGuiltTrip getAddressBook();

    /**
     * Returns true if a entry with the same identity as {@code entry} exists in
     * the guilttrip book.
     */
    boolean hasCategory(Category category);

    /**
     * Returns true if a entry with the same identity as {@code entry} exists in
     * the guilttrip book.
     */
    boolean hasEntry(Entry entry);

    boolean hasReminder(Reminder reminder);

    boolean hasCondition(Condition condition);

    boolean hasBudget(Budget budget);

    boolean hasWish(Wish wish);

    /**
     * Deletes the given category. The category must exist in the guilttrip book.
     */
    void deleteCategory(Category target);

    /**
     * Deletes the given entry. The entry must exist in the guilttrip book.
     */
    void deleteEntry(Entry target);

    /**
     * Deletes the given expense. The entry must exist in the guilttrip book.
     */
    void deleteExpense(Expense target);

    /**
     * Deletes the given income. The income must exist in the guilttrip book.
     */
    void deleteIncome(Income target);

    /**
     * Deletes the given wish. The wish must exist in the guilttrip book.
     */
    void deleteWish(Wish target);

    void deleteReminder(Reminder target);

    void deleteCondition(Condition target);

    /**
     * Deletes the given budget.
     * The budget must exist in the guilttrip book.
     */
    void deleteBudget(Budget target);

    /**
     * Deletes the given AutoExpense. The entry must exist in the guilttrip book.
     */
    void deleteAutoExpense(AutoExpense target);

    /**
     * Adds the given entry. {@code entry} must not already exist in the guilttrip
     * book.
     */
    void addEntry(Entry entry);

    void addCategory(Category category);

    void addExpense(Expense expense);

    void addIncome(Income income);

    void addWish(Wish wish);

    void addBudget(Budget budget);

    void addAutoExpense(AutoExpense autoExpense);

    void addReminder(Reminder reminder);

    void addCondition(Condition condition);

    void setCategory(Category target, Category editedCategory);

    /**
     * Replaces the given entry {@code target} with {@code editedEntry}.
     * {@code target} must exist in the guilttrip book. The entry identity of
     * {@code editedEntry} must not be the same as another existing entry in the
     * guilttrip book.
     */
    void setEntry(Entry target, Entry editedEntry);


    void setReminder(Reminder target, Reminder editedEntry);

    void setCondition(Condition target, Condition editedEntry);

    void setIncome(Income target, Income editedEntry);

    void setExpense(Expense target, Expense editedEntry);

    void setWish(Wish target, Wish editedWish);

    void setBudget(Budget target, Budget editedbudget);


    CategoryList getCategoryList();

    /** Returns an unmodifiable view of the income category list */
    ObservableList<Category> getIncomeCategoryList();

    /** Returns an unmodifiable view of the expense category list */
    ObservableList<Category> getExpenseCategoryList();

    /** Returns an unmodifiable view of the filtered entry list */
    ObservableList<Entry> getFilteredEntryList();

    /** Returns an unmodifiable view of the filtered expenditure list */
    ObservableList<Expense> getFilteredExpenses();

    /** Returns an unmodifiable view of the filtered entry list */
    ObservableList<Income> getFilteredIncomes();

    /** Returns an unmodifiable view of filtered expense and income list */
    ObservableList<Entry> getFilteredExpensesAndIncomes();

    /** Returns an unmodifiable view of the filtered entry list */
    ObservableList<Wish> getFilteredWishes();

    /** Returns an unmodifiable view of the filtered budget list */
    ObservableList<Budget> getFilteredBudgets();

    /** Returns an unmodifiable view of the filtered expenditure list */
    ObservableList<AutoExpense> getFilteredAutoExpenses();

    /** Returns an unmodifiable view of the filtered reminder list */
    ObservableList<Reminder> getFilteredReminders();

    /** Returns an unmodifiable view of the filtered condition list */
    ObservableList<Condition> getFilteredConditions();

    /**
     * Updates the filter of the filtered entry list to filter by the given
     * {@code predicate}.
     *
     * @throws NullPointerException if {@code predicate} is null.
     */
    void updateFilteredEntryList(Predicate<Entry> predicate);

    void updateFilteredExpenses(Predicate<Expense> predicate);

    void updateFilteredIncomes(Predicate<Income> predicate);

    void updateFilteredWishes(Predicate<Entry> predicate);

    void updateFilteredBudgets(Predicate<Entry> predicate);

    void updateFilteredAutoExpenses(Predicate<AutoExpense> predicate);

    void updateFilteredReminders(Predicate<Reminder> predicate);

    void sortFilteredEntry(SortType comparator, SortSequence sequence);

    /**
     * Returns true if the model has previous finance tracker states to restore.
     */
    boolean canUndoAddressBook(Step step);

    /**
     * Returns true if the model has undone finance tracker states to restore.
     */
    boolean canRedoAddressBook(Step step);

    /**
     * Restores the model's finance tracker to its previous state.
     */
    void undoAddressBook();

    /**
     * Restores the model's finance tracker to its previously undone state.
     */
    void redoAddressBook();

    /**
     * Saves the current finance tracker state for undo/redo
     */
    void commitAddressBook();

}
