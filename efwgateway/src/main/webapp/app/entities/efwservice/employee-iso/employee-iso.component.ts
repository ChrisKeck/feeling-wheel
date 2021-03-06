import {HttpErrorResponse, HttpHeaders, HttpResponse} from '@angular/common/http';
import {Component, OnDestroy, OnInit} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {AccountService} from 'app/core';

import {ITEMS_PER_PAGE} from 'app/shared';

import {IEmployeeIso} from 'app/shared/model/efwservice/employee-iso.model';
import {JhiAlertService, JhiEventManager, JhiParseLinks} from 'ng-jhipster';
import {Subscription} from 'rxjs';
import {EmployeeIsoService} from './employee-iso.service';

@Component({
               selector: 'jhi-employee-iso', templateUrl: './employee-iso.component.html'
           })
export class EmployeeIsoComponent implements OnInit, OnDestroy {
    employees: IEmployeeIso[];
    currentAccount: any;
    eventSubscriber: Subscription;
    itemsPerPage: number;
    links: any;
    page: any;
    predicate: any;
    reverse: any;
    totalItems: number;
    currentSearch: string;

    constructor(protected employeeService: EmployeeIsoService,
                protected jhiAlertService: JhiAlertService,
                protected eventManager: JhiEventManager,
                protected parseLinks: JhiParseLinks,
                protected activatedRoute: ActivatedRoute,
                protected accountService: AccountService) {
        this.employees = [];
        this.itemsPerPage = ITEMS_PER_PAGE;
        this.page = 0;
        this.links = {
            last: 0
        };
        this.predicate = 'id';
        this.reverse = true;
        this.currentSearch = this.activatedRoute.snapshot && this.activatedRoute.snapshot.params['search'] ?
                             this.activatedRoute.snapshot.params['search'] :
                             '';
    }

    loadAll() {
        if (this.currentSearch) {
            this.employeeService
                .search({
                            query: this.currentSearch, page: this.page, size: this.itemsPerPage, sort: this.sort()
                        })
                .subscribe((res: HttpResponse<IEmployeeIso[]>) => this.paginateEmployees(res.body, res.headers),
                           (res: HttpErrorResponse) => this.onError(res.message));
            return;
        }
        this.employeeService
            .query({
                       page: this.page, size: this.itemsPerPage, sort: this.sort()
                   })
            .subscribe((res: HttpResponse<IEmployeeIso[]>) => this.paginateEmployees(res.body, res.headers),
                       (res: HttpErrorResponse) => this.onError(res.message));
    }

    reset() {
        this.page = 0;
        this.employees = [];
        this.loadAll();
    }

    loadPage(page) {
        this.page = page;
        this.loadAll();
    }

    clear() {
        this.employees = [];
        this.links = {
            last: 0
        };
        this.page = 0;
        this.predicate = 'id';
        this.reverse = true;
        this.currentSearch = '';
        this.loadAll();
    }

    search(query) {
        if (!query) {
            return this.clear();
        }
        this.employees = [];
        this.links = {
            last: 0
        };
        this.page = 0;
        this.predicate = '_score';
        this.reverse = false;
        this.currentSearch = query;
        this.loadAll();
    }

    ngOnInit() {
        this.loadAll();
        this.accountService.identity().then(account => {
            this.currentAccount = account;
        });
        this.registerChangeInEmployees();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: IEmployeeIso) {
        return item.id;
    }

    registerChangeInEmployees() {
        this.eventSubscriber = this.eventManager.subscribe('employeeListModification', response => this.reset());
    }

    sort() {
        const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
        if (this.predicate !== 'id') {
            result.push('id');
        }
        return result;
    }

    protected paginateEmployees(data: IEmployeeIso[], headers: HttpHeaders) {
        this.links = this.parseLinks.parse(headers.get('link'));
        this.totalItems = parseInt(headers.get('X-Total-Count'), 10);
        for (let i = 0; i < data.length; i++) {
            this.employees.push(data[i]);
        }
    }

    protected onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}
