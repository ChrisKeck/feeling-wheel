/* tslint:disable no-unused-expression */
import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../../page-objects/jhi-page-objects';

import { FeelingComponentsPage, FeelingDeleteDialog, FeelingUpdatePage } from './feeling-iso.page-object';

const expect = chai.expect;

describe('Feeling e2e test', () => {
    let navBarPage: NavBarPage;
    let signInPage: SignInPage;
    let feelingUpdatePage: FeelingUpdatePage;
    let feelingComponentsPage: FeelingComponentsPage;
    let feelingDeleteDialog: FeelingDeleteDialog;

    before(async () => {
        await browser.get('/');
        navBarPage = new NavBarPage();
        signInPage = await navBarPage.getSignInPage();
        await signInPage.autoSignInUsing('admin', 'admin');
        await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
    });

    it('should load Feelings', async () => {
        await navBarPage.goToEntity('feeling-iso');
        feelingComponentsPage = new FeelingComponentsPage();
        await browser.wait(ec.visibilityOf(feelingComponentsPage.title), 5000);
        expect(await feelingComponentsPage.getTitle()).to.eq('efwgatewayApp.efwserviceFeeling.home.title');
    });

    it('should load create Feeling page', async () => {
        await feelingComponentsPage.clickOnCreateButton();
        feelingUpdatePage = new FeelingUpdatePage();
        expect(await feelingUpdatePage.getPageTitle()).to.eq('efwgatewayApp.efwserviceFeeling.home.createOrEditLabel');
        await feelingUpdatePage.cancel();
    });

    it('should create and save Feelings', async () => {
        const nbButtonsBeforeCreate = await feelingComponentsPage.countDeleteButtons();

        await feelingComponentsPage.clickOnCreateButton();
        await promise.all([
            feelingUpdatePage.feeltypeSelectLastOption(),
            feelingUpdatePage.setCapacityInput('5'),
            feelingUpdatePage.feelwheelSelectLastOption()
        ]);
        expect(await feelingUpdatePage.getCapacityInput()).to.eq('5');
        const selectedIsSpeechable = feelingUpdatePage.getIsSpeechableInput();
        if (await selectedIsSpeechable.isSelected()) {
            await feelingUpdatePage.getIsSpeechableInput().click();
            expect(await feelingUpdatePage.getIsSpeechableInput().isSelected()).to.be.false;
        } else {
            await feelingUpdatePage.getIsSpeechableInput().click();
            expect(await feelingUpdatePage.getIsSpeechableInput().isSelected()).to.be.true;
        }
        await feelingUpdatePage.save();
        expect(await feelingUpdatePage.getSaveButton().isPresent()).to.be.false;

        expect(await feelingComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1);
    });

    it('should delete last Feeling', async () => {
        const nbButtonsBeforeDelete = await feelingComponentsPage.countDeleteButtons();
        await feelingComponentsPage.clickOnLastDeleteButton();

        feelingDeleteDialog = new FeelingDeleteDialog();
        expect(await feelingDeleteDialog.getDialogTitle()).to.eq('efwgatewayApp.efwserviceFeeling.delete.question');
        await feelingDeleteDialog.clickOnConfirmButton();

        expect(await feelingComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
    });

    after(async () => {
        await navBarPage.autoSignOut();
    });
});
