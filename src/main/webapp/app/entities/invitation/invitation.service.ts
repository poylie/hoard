import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';

import { Invitation } from './invitation.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class InvitationService {

    private resourceUrl = 'api/invitations';
    private resourceSearchUrl = 'api/_search/invitations';

    constructor(private http: Http) { }

    create(invitation: Invitation): Observable<Invitation> {
        const copy = this.convert(invitation);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            return res.json();
        });
    }

    update(invitation: Invitation): Observable<Invitation> {
        const copy = this.convert(invitation);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            return res.json();
        });
    }

    find(id: number): Observable<Invitation> {
        return this.http.get(`${this.resourceUrl}/${id}`).map((res: Response) => {
            return res.json();
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
        return new ResponseWrapper(res.headers, jsonResponse, res.status);
    }

    private convert(invitation: Invitation): Invitation {
        const copy: Invitation = Object.assign({}, invitation);
        return copy;
    }
}
