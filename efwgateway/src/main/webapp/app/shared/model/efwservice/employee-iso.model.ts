import { IFeelWheelIso } from 'app/shared/model/efwservice/feel-wheel-iso.model';
import { IEmployeeIso } from 'app/shared/model/efwservice/employee-iso.model';

export interface IEmployeeIso {
    id?: number;
    email?: string;
    feelWheels?: IFeelWheelIso[];
    employees?: IEmployeeIso[];
    employeeEmail?: string;
    employeeId?: number;
}

export class EmployeeIso implements IEmployeeIso {
    constructor(
        public id?: number,
        public email?: string,
        public feelWheels?: IFeelWheelIso[],
        public employees?: IEmployeeIso[],
        public employeeEmail?: string,
        public employeeId?: number
    ) {}
}
