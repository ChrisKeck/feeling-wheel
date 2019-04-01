import {NgbActiveModal} from '@ng-bootstrap/ng-bootstrap';
import {SpyObject} from './spyobject';
import Spy = jasmine.Spy;

export class MockActiveModal extends SpyObject {
    dismissSpy: Spy;

    constructor() {
        super(NgbActiveModal);
        this.dismissSpy = this.spy('dismiss').andReturn(this);
    }
}
