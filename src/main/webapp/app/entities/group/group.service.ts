import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';

import { Group } from './group.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class GroupService {

    private resourceUrl = 'api/groups';
    private resourceUrlCurrentUser = 'api/groupsForUser';
    private resourceSearchUrl = 'api/_search/groups';
    private resourceSearchUrlCurrentUser = 'api/_search/groupsCurrentuser';

    currentGroup: Group;

    constructor(private http: Http) { }

    create(group: Group): Observable<Group> {
        const copy = this.convert(group);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            return res.json();
        });
    }

    update(group: Group): Observable<Group> {
        const copy = this.convert(group);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            return res.json();
        });
    }

    find(id: number): Observable<Group> {
        return this.http.get(`${this.resourceUrl}/${id}`).map((res: Response) => {
            return res.json();
        });
    }

    query(req?: any): Observable<ResponseWrapper> {
        const options = createRequestOption(req);
        return this.http.get(this.resourceUrlCurrentUser, options)
            .map((res: Response) => this.convertResponse(res));
    }

    delete(id: number): Observable<Response> {
        return this.http.delete(`${this.resourceUrl}/${id}`);
    }

    search(req?: any): Observable<ResponseWrapper> {
        const options = createRequestOption(req);
        return this.http.get(this.resourceSearchUrlCurrentUser, options)
            .map((res: any) => this.convertResponse(res));
    }

    private convertResponse(res: Response): ResponseWrapper {
        const jsonResponse = res.json();
        return new ResponseWrapper(res.headers, jsonResponse, res.status);
    }

    private convert(group: Group): Group {
        const copy: Group = Object.assign({}, group);
        return copy;
    }

    setCurrentGroup(group: Group) {
        this.currentGroup = group;
    }

    clearCurrentGroup() {
        console.log('cleared');
        this.currentGroup = null;
    }

    getCurrentGroup(): Group {
        return this.currentGroup;
    }
}
