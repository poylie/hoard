import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils } from 'ng-jhipster';

import { SalesOrder } from './sales-order.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class SalesOrderService {

    private resourceUrl = 'api/sales-orders';
    private resourceSearchUrl = 'api/_search/sales-orders';

    constructor(private http: Http, private dateUtils: JhiDateUtils) { }

    create(salesOrder: SalesOrder): Observable<SalesOrder> {
        const copy = this.convert(salesOrder);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            this.convertItemFromServer(jsonResponse);
            return jsonResponse;
        });
    }

    update(salesOrder: SalesOrder): Observable<SalesOrder> {
        const copy = this.convert(salesOrder);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            this.convertItemFromServer(jsonResponse);
            return jsonResponse;
        });
    }

    find(id: number): Observable<SalesOrder> {
        return this.http.get(`${this.resourceUrl}/${id}`).map((res: Response) => {
            const jsonResponse = res.json();
            this.convertItemFromServer(jsonResponse);
            return jsonResponse;
        });
    }

    query(req?: any): Observable<ResponseWrapper> {
        const options = createRequestOption(req);
        return this.http.get(this.resourceUrl, options)
            .map((res: Response) => this.convertResponse(res));
    }

    delete(id: number): Observable<Response> {
        return this.http.delete(`${this.resourceUrl}/${id}`);
    }

    search(req?: any): Observable<ResponseWrapper> {
        const options = createRequestOption(req);
        return this.http.get(this.resourceSearchUrl, options)
            .map((res: any) => this.convertResponse(res));
    }

    private convertResponse(res: Response): ResponseWrapper {
        const jsonResponse = res.json();
        for (let i = 0; i < jsonResponse.length; i++) {
            this.convertItemFromServer(jsonResponse[i]);
        }
        return new ResponseWrapper(res.headers, jsonResponse, res.status);
    }

    private convertItemFromServer(entity: any) {
        entity.transactionDate = this.dateUtils
            .convertLocalDateFromServer(entity.transactionDate);
        entity.lastUpdated = this.dateUtils
            .convertDateTimeFromServer(entity.lastUpdated);
    }

    private convert(salesOrder: SalesOrder): SalesOrder {
        const copy: SalesOrder = Object.assign({}, salesOrder);
        copy.transactionDate = this.dateUtils
            .convertLocalDateToServer(salesOrder.transactionDate);

        copy.lastUpdated = this.dateUtils.toDate(salesOrder.lastUpdated);
        return copy;
    }
}
