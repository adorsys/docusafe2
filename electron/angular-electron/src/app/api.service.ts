import {Injectable} from '@angular/core';
import {HttpClient, HttpRequest, HttpResponse} from "@angular/common/http";
import {Observable, of} from "rxjs";
import {flatMap, map} from "rxjs/operators";
import {Credentials} from "./credentials.service";

@Injectable({providedIn: 'root'})
export class ApiService {

    private static TOKEN_HEADER = "token";

    public apiUserName = "root";
    public apiPassword = "root";

    private uri = "http://localhost:8080";
    private authorizeUri = this.uri + "/api/authenticate";
    private createUserUri = this.uri + "/user";
    private listDocumentUri = this.uri + "/documents";
    private putDocumentUri = this.uri + "/document/";
    private getDocumentUri = this.uri + "/document/";
    private deleteDocumentUri = this.uri + "/document/";

    private token: string;

    constructor(private httpClient: HttpClient) {
    }

    public authorize() {
        let result = this.httpClient.post(
            this.authorizeUri,
            {"userName": this.apiUserName, "password": this.apiPassword},
            {observe: 'response'}
        );

        result.subscribe(res => {
            this.token = ApiService.extractToken(res)
        });

        return result;
    }

    public createUser(username: string, password: string) {
        return this.withAuthorization()
            .pipe(flatMap(token =>
                this.httpClient.put(
                    this.createUserUri,
                    {"userName": username, "password": password},
                    ApiService.headers(token)
            ))).toPromise();
    }

    public listDocuments(path: string, creds: Credentials) {
        return this.withAuthorization()
            .pipe(flatMap(token =>
                this.httpClient.get(
                    this.listDocumentUri + path,
                    ApiService.headersWithAuth(token, creds)
                ))).toPromise();
    }

    public uploadDocument(document, path: string, creds: Credentials) {
        let formData: FormData = new FormData();
        formData.append('file', document);

        return this.withAuthorization()
            .pipe(flatMap(token =>
                this.httpClient.put(
                    this.putDocumentUri + path,
                    formData,
                    {
                        "headers": ApiService.headersWithAuth(token, creds)["headers"],
                        responseType: 'blob' as 'json'
                    })
            )).toPromise();
    }


    public downloadDocument(path: string, creds: Credentials) {
        this.withAuthorization()
            .pipe(flatMap(token =>
                this.httpClient.get(
                    this.getDocumentUri + path,
                    {
                        "headers": ApiService.headersWithAuth(token, creds)["headers"],
                        responseType: 'blob' as 'json'
                    }
                )
            )).subscribe(
            (response: any) => {
                let dataType = response.type;
                let binaryData = [];
                binaryData.push(response);
                let downloadLink = document.createElement('a');
                downloadLink.href = window.URL.createObjectURL(new Blob(binaryData, {type: dataType}));
                downloadLink.setAttribute('download', path.match(/(.+\/)*([^/]+)$/)[2]);
                document.body.appendChild(downloadLink);
                downloadLink.click();
            }
        )
    }

    public deleteDocument(path: string, creds: Credentials) {
        return this.withAuthorization()
            .pipe(flatMap(token =>
                this.httpClient.delete(
                    this.deleteDocumentUri + path,
                    ApiService.headersWithAuth(token, creds)
                ))).toPromise();
    }

    private withAuthorization() : Observable<string> {
        if (null == this.token) {
            return this.authorize()
                .pipe(map((res) => ApiService.extractToken(res)))
        }

        return of(this.token)
    }

    private static headers(token: string) {
        return {"headers": {[ApiService.TOKEN_HEADER]: token}};
    }

    private static headersWithAuth(token: string, creds: Credentials) {
        return {"headers": {
                [ApiService.TOKEN_HEADER]: token,
                "user": creds.username,
                "password": creds.password}
        };
    }

    private static extractToken(response: HttpResponse<Object>) : string {
        return response.headers.get(ApiService.TOKEN_HEADER)
    }
}
