/* tslint:disable no-unused-expression */
import {browser, ExpectedConditions as ec, promise, protractor} from 'protractor';
import {NavBarPage, SignInPage} from '../../../page-objects/jhi-page-objects';

import {FeelWheelComponentsPage, FeelWheelDeleteDialog, FeelWheelUpdatePage} from './feel-wheel-iso.page-object';

const expect = chai.expect;

describe('FeelWheel e2e test', () => {
    let navBarPage: NavBarPage;
    let signInPage: SignInPage;
    let feelWheelUpdatePage: FeelWheelUpdatePage;
    let feelWheelComponentsPage: FeelWheelComponentsPage;
    let feelWheelDeleteDialog: FeelWheelDeleteDialog;

    before(async () => {
        await browser.get('/');
        navBarPage = new NavBarPage();
        signInPage = await navBarPage.getSignInPage();
        await signInPage.autoSignInUsing('admin', 'admin');
        await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
    });

    it('should load FeelWheels', async () => {
        await navBarPage.goToEntity('feel-wheel-iso');
        feelWheelComponentsPage = new FeelWheelComponentsPage();
        await browser.wait(ec.visibilityOf(feelWheelComponentsPage.title), 5000);
        expect(await feelWheelComponentsPage.getTitle()).to.eq('efwgatewayApp.efwserviceFeelWheel.home.title');
    });

    it('should load create FeelWheel page', async () => {
        await feelWheelComponentsPage.clickOnCreateButton();
        feelWheelUpdatePage = new FeelWheelUpdatePage();
        expect(await feelWheelUpdatePage.getPageTitle()).to.eq('efwgatewayApp.efwserviceFeelWheel.home.createOrEditLabel');
        await feelWheelUpdatePage.cancel();
    });

    it('should create and save FeelWheels', async () => {
        const nbButtonsBeforeCreate = await feelWheelComponentsPage.countDeleteButtons();

        await feelWheelComponentsPage.clickOnCreateButton();
        await promise.all([feelWheelUpdatePage.setSubjectInput('subject'),
                           feelWheelUpdatePage.setFromInput('01/01/2001' + protractor.Key.TAB + '02:30AM'),
                           feelWheelUpdatePage.setToInput('01/01/2001' + protractor.Key.TAB + '02:30AM'),
                           feelWheelUpdatePage.employeeSelectLastOption()]);
        expect(await feelWheelUpdatePage.getSubjectInput()).to.eq('subject');
        expect(await feelWheelUpdatePage.getFromInput()).to.contain('2001-01-01T02:30');
        expect(await feelWheelUpdatePage.getToInput()).to.contain('2001-01-01T02:30');
        await feelWheelUpdatePage.save();
        expect(await feelWheelUpdatePage.getSaveButton().isPresent()).to.be.false;

        expect(await feelWheelComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1);
    });

    it('should delete last FeelWheel', async () => {
        const nbButtonsBeforeDelete = await feelWheelComponentsPage.countDeleteButtons();
        await feelWheelComponentsPage.clickOnLastDeleteButton();

        feelWheelDeleteDialog = new FeelWheelDeleteDialog();
        expect(await feelWheelDeleteDialog.getDialogTitle()).to.eq('efwgatewayApp.efwserviceFeelWheel.delete.question');
        await feelWheelDeleteDialog.clickOnConfirmButton();

        expect(await feelWheelComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
    });

    after(async () => {
        await navBarPage.autoSignOut();
    });
});
