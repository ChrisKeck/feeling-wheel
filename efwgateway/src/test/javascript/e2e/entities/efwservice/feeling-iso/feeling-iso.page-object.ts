import { element, by, ElementFinder } from 'protractor';

export class FeelingComponentsPage {
    createButton = element(by.id('jh-create-entity'));
    deleteButtons = element.all(by.css('jhi-feeling-iso div table .btn-danger'));
    title = element.all(by.css('jhi-feeling-iso div h2#page-heading span')).first();

    async clickOnCreateButton() {
        await this.createButton.click();
    }

    async clickOnLastDeleteButton() {
        await this.deleteButtons.last().click();
    }

    async countDeleteButtons() {
        return this.deleteButtons.count();
    }

    async getTitle() {
        return this.title.getAttribute('jhiTranslate');
    }
}

export class FeelingUpdatePage {
    pageTitle = element(by.id('jhi-feeling-iso-heading'));
    saveButton = element(by.id('save-entity'));
    cancelButton = element(by.id('cancel-save'));
    feeltypeSelect = element(by.id('field_feeltype'));
    capacityInput = element(by.id('field_capacity'));
    isSpeechableInput = element(by.id('field_isSpeechable'));
    feelwheelSelect = element(by.id('field_feelwheel'));

    async getPageTitle() {
        return this.pageTitle.getAttribute('jhiTranslate');
    }

    async setFeeltypeSelect(feeltype) {
        await this.feeltypeSelect.sendKeys(feeltype);
    }

    async getFeeltypeSelect() {
        return this.feeltypeSelect.element(by.css('option:checked')).getText();
    }

    async feeltypeSelectLastOption() {
        await this.feeltypeSelect
            .all(by.tagName('option'))
            .last()
            .click();
    }

    async setCapacityInput(capacity) {
        await this.capacityInput.sendKeys(capacity);
    }

    async getCapacityInput() {
        return this.capacityInput.getAttribute('value');
    }

    getIsSpeechableInput() {
        return this.isSpeechableInput;
    }

    async feelwheelSelectLastOption() {
        await this.feelwheelSelect
            .all(by.tagName('option'))
            .last()
            .click();
    }

    async feelwheelSelectOption(option) {
        await this.feelwheelSelect.sendKeys(option);
    }

    getFeelwheelSelect(): ElementFinder {
        return this.feelwheelSelect;
    }

    async getFeelwheelSelectedOption() {
        return this.feelwheelSelect.element(by.css('option:checked')).getText();
    }

    async save() {
        await this.saveButton.click();
    }

    async cancel() {
        await this.cancelButton.click();
    }

    getSaveButton(): ElementFinder {
        return this.saveButton;
    }
}

export class FeelingDeleteDialog {
    private dialogTitle = element(by.id('jhi-delete-feeling-heading'));
    private confirmButton = element(by.id('jhi-confirm-delete-feeling'));

    async getDialogTitle() {
        return this.dialogTitle.getAttribute('jhiTranslate');
    }

    async clickOnConfirmButton() {
        await this.confirmButton.click();
    }
}
