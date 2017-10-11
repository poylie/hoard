import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';

import { UserGroup } from './user-group.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class UserGroupService {

    private resourceUrl = 'api/user-groups';
    private resourceUrlCurrentGroup = 'api/user-groups/current-group';
    private resourceSearchUrl = 'api/_search/user-groups';

    constructor(private http: Http) { }

    create(userGroup: UserGroup): Observable<UserGroup> {
        const copy = this.convert(userGroup);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            return res.json();
        });
    }

    update(userGroup: UserGroup): Observable<UserGroup> {
        const copy = this.convert(userGroup);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            return res.json();
        });
    }

    find(id: number): Observable<UserGroup> {
        return this.http.get(`${this.resourceUrl}/${id}`).map((res: Response) => {
            return res.json();
        });
    }

    query(groupId: number, req?: any): Observable<ResponseWrapper> {
        const options = createRequestOption(req);
        return this.http.get(`${this.resourceUrlCurrentGroup}/${groupId}`, options)
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

    private convert(userGroup: UserGroup): UserGroup {
        const copy: UserGroup = Object.assign({}, userGroup);
        return copy;
    }
}
