import {Component, OnInit} from '@angular/core';
import {NgbModalRef} from '@ng-bootstrap/ng-bootstrap';

import {Account, AccountService, LoginModalService} from 'app/core';
import {JhiEventManager} from 'ng-jhipster';

@Component({
               selector: 'jhi-home', templateUrl: './home.component.html', styleUrls: ['home.component.scss']
           })
export class HomeComponent implements OnInit {
    account: Account;
    modalRef: NgbModalRef;

    constructor(private accountService: AccountService, private loginModalService: LoginModalService, private eventManager: JhiEventManager) {
    }

    ngOnInit() {
        this.accountService.identity().then((account: Account) => {
            this.account = account;
        });
        this.registerAuthenticationSuccess();
    }

    registerAuthenticationSuccess() {
        this.eventManager.subscribe('authenticationSuccess', message => {
            this.accountService.identity().then(account => {
                this.account = account;
            });
        });
    }

    isAuthenticated() {
        return this.accountService.isAuthenticated();
    }

    login() {
        this.modalRef = this.loginModalService.open();
    }
}
