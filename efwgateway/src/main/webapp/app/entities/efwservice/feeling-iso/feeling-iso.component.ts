import {HttpErrorResponse, HttpHeaders, HttpResponse} from '@angular/common/http';
import {Component, OnDestroy, OnInit} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {AccountService} from 'app/core';

import {ITEMS_PER_PAGE} from 'app/shared';

import {IFeelingIso} from 'app/shared/model/efwservice/feeling-iso.model';
import {JhiAlertService, JhiEventManager, JhiParseLinks} from 'ng-jhipster';
import {Subscription} from 'rxjs';
import {FeelingIsoService} from './feeling-iso.service';

@Component({
               selector: 'jhi-feeling-iso', templateUrl: './feeling-iso.component.html'
           })
export class FeelingIsoComponent implements OnInit, OnDestroy {
    feelings: IFeelingIso[];
    currentAccount: any;
    eventSubscriber: Subscription;
    itemsPerPage: number;
    links: any;
    page: any;
    predicate: any;
    reverse: any;
    totalItems: number;
    currentSearch: string;

    constructor(protected feelingService: FeelingIsoService,
                protected jhiAlertService: JhiAlertService,
                protected eventManager: JhiEventManager,
                protected parseLinks: JhiParseLinks,
                protected activatedRoute: ActivatedRoute,
                protected accountService: AccountService) {
        this.feelings = [];
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
            this.feelingService
                .search({
                            query: this.currentSearch, page: this.page, size: this.itemsPerPage, sort: this.sort()
                        })
                .subscribe((res: HttpResponse<IFeelingIso[]>) => this.paginateFeelings(res.body, res.headers),
                           (res: HttpErrorResponse) => this.onError(res.message));
            return;
        }
        this.feelingService
            .query({
                       page: this.page, size: this.itemsPerPage, sort: this.sort()
                   })
            .subscribe((res: HttpResponse<IFeelingIso[]>) => this.paginateFeelings(res.body, res.headers),
                       (res: HttpErrorResponse) => this.onError(res.message));
    }

    reset() {
        this.page = 0;
        this.feelings = [];
        this.loadAll();
    }

    loadPage(page) {
        this.page = page;
        this.loadAll();
    }

    clear() {
        this.feelings = [];
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
        this.feelings = [];
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
        this.registerChangeInFeelings();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: IFeelingIso) {
        return item.id;
    }

    registerChangeInFeelings() {
        this.eventSubscriber = this.eventManager.subscribe('feelingListModification', response => this.reset());
    }

    sort() {
        const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
        if (this.predicate !== 'id') {
            result.push('id');
        }
        return result;
    }

    protected paginateFeelings(data: IFeelingIso[], headers: HttpHeaders) {
        this.links = this.parseLinks.parse(headers.get('link'));
        this.totalItems = parseInt(headers.get('X-Total-Count'), 10);
        for (let i = 0; i < data.length; i++) {
            this.feelings.push(data[i]);
        }
    }

    protected onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}
