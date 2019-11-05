package seedu.address.logic.commands.editcommands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_AMOUNT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_CATEGORY;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DESC;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_ENTRIES;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_INCOMES;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.CollectionUtil;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.entry.Amount;
import seedu.address.model.entry.Category;
import seedu.address.model.entry.Date;
import seedu.address.model.entry.Description;
import seedu.address.model.entry.Income;
import seedu.address.model.tag.Tag;

/**
 * Edits the details of an existing income in the address book.
 */
public class EditIncomeCommand extends Command {

    public static final String COMMAND_WORD = "editIncome";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Edits the details of the Income identified "
            + "by the index number used in the displayed Incomes list. "
            + "Existing values will be overwritten by the input values.\n"
            + "Parameters: INDEX (must be a positive integer) "
            + "[" + PREFIX_CATEGORY + "CATEGORY] "
            + "[" + PREFIX_DESC + "NAME] "
            + "[" + PREFIX_DATE + "DATE] "
            + "[" + PREFIX_AMOUNT + "AMOUNT] "
            + "[" + PREFIX_TAG + "TAG]...\n"
            + "Example: " + COMMAND_WORD + " 1 "
            + PREFIX_AMOUNT + "3500";

    public static final String MESSAGE_EDIT_ENTRY_SUCCESS = "Edited Income: %1$s";
    public static final String MESSAGE_NOT_EDITED = "At least one field to edit must be provided.";
    public static final String MESSAGE_DUPLICATE_ENTRY = "This income already exists in guiltTrip.";

    private final Index index;
    private final EditIncomeDescriptor editIncomeDescriptor;

    /**
     * @param index of the income in the filtered income list to edit
     * @param editIncomeDescriptor details to edit the income with
     */
    public EditIncomeCommand(Index index, EditIncomeDescriptor editIncomeDescriptor) {
        requireNonNull(index);
        requireNonNull(editIncomeDescriptor);

        this.index = index;
        this.editIncomeDescriptor = new EditIncomeDescriptor(editIncomeDescriptor);
    }

    @Override
    public CommandResult execute(Model model, CommandHistory history) throws CommandException {
        requireNonNull(model);
        List<Income> lastShownList = model.getFilteredIncomes();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_ENTRY_DISPLAYED_INDEX);
        }

        Income incomeToEdit = lastShownList.get(index.getZeroBased());
        Income editedIncome = createEditedIncome(incomeToEdit, editIncomeDescriptor);

        if (incomeToEdit.isSameEntry(editedIncome) && model.hasEntry(editedIncome)) {
            throw new CommandException(MESSAGE_DUPLICATE_ENTRY);
        }

        model.setEntry(incomeToEdit, editedIncome);
        model.updateFilteredIncomes(PREDICATE_SHOW_ALL_INCOMES);
        model.updateFilteredEntryList(PREDICATE_SHOW_ALL_ENTRIES);
        model.commitAddressBook();
        return new CommandResult(String.format(MESSAGE_EDIT_ENTRY_SUCCESS, editedIncome));
    }

    /**
     * Creates and returns a {@code Income} with the details of {@code incomeToEdit}
     * edited with {@code editIncomeDescriptor}.
     */
    private static Income createEditedIncome(Income incomeToEdit, EditIncomeDescriptor editIncomeDescriptor) {
        assert incomeToEdit != null;
        Category updatedCategory = editIncomeDescriptor.getCategory().orElse(incomeToEdit.getCategory());
        Description updatedName = editIncomeDescriptor.getDesc().orElse(incomeToEdit.getDesc());
        Date updatedTime = editIncomeDescriptor.getTime().orElse(incomeToEdit.getDate());
        Amount updatedAmount = editIncomeDescriptor.getAmount().orElse(incomeToEdit.getAmount());
        Set<Tag> updatedTags = editIncomeDescriptor.getTags().orElse(incomeToEdit.getTags());
        return new Income(updatedCategory, updatedName, updatedTime, updatedAmount, updatedTags);
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof EditIncomeCommand)) {
            return false;
        }

        // state check
        EditIncomeCommand e = (EditIncomeCommand) other;
        return index.equals(e.index)
                && editIncomeDescriptor.equals(e.editIncomeDescriptor);
    }

    /**
     * Stores the details to edit the Income with. Each non-empty field value will replace the
     * corresponding field value of the income.
     */
    public static class EditIncomeDescriptor {
        private Category category;
        private Description desc;
        private Date date;
        private Amount amt;
        private Set<Tag> tags;

        public EditIncomeDescriptor() {}

        /**
         * Copy constructor.
         * A defensive copy of {@code tags} is used internally.
         */
        public EditIncomeDescriptor(EditIncomeDescriptor toCopy) {
            setCategory(toCopy.category);
            setDesc(toCopy.desc);
            setTime(toCopy.date);
            setAmount(toCopy.amt);
            setTags(toCopy.tags);
        }

        /**
         * Returns true if at least one field is edited.
         */
        public boolean isAnyFieldEdited() {
            return CollectionUtil.isAnyNonNull(category, desc, date, amt, tags);
        }

        public void setCategory(Category cat) {
            this.category = cat;
        }

        public Optional<Category> getCategory() {
            return Optional.ofNullable(category);
        }

        public void setDesc(Description desc) {
            this.desc = desc;
        }

        public Optional<Description> getDesc() {
            return Optional.ofNullable(desc);
        }

        public void setTime(Date time) {
            this.date = time;
        }

        public Optional<Date> getTime() {
            return Optional.ofNullable(date);
        }

        public void setAmount(Amount amt) {
            this.amt = amt;
        }

        public Optional<Amount> getAmount() {
            return Optional.ofNullable(amt);
        }


        /**
         * Sets {@code tags} to this object's {@code tags}.
         * A defensive copy of {@code tags} is used internally.
         */
        public void setTags(Set<Tag> tags) {
            this.tags = (tags != null) ? new HashSet<>(tags) : null;
        }

        /**
         * Returns an unmodifiable tag set, which throws {@code UnsupportedOperationException}
         * if modification is attempted.
         * Returns {@code Optional#empty()} if {@code tags} is null.
         */
        public Optional<Set<Tag>> getTags() {
            return (tags != null) ? Optional.of(Collections.unmodifiableSet(tags)) : Optional.empty();
        }

        @Override
        public boolean equals(Object other) {
            // short circuit if same object
            if (other == this) {
                return true;
            }

            // instanceof handles nulls
            if (!(other instanceof EditIncomeDescriptor)) {
                return false;
            }

            // state check
            EditIncomeDescriptor e = (EditIncomeDescriptor) other;

            return getDesc().equals(e.getDesc())
                    && getAmount().equals(e.getAmount())
                    && getTime().equals(e.getTime())
                    && getTags().equals(e.getTags());
        }
    }
}
