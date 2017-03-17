package com.landicorp.android.wofupay.volley;



import com.landicorp.android.wofupay.bean.PosADList;
import com.yolanda.nohttp.Headers;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.download.DownloadConnection;
import com.yolanda.nohttp.download.DownloadListener;
import com.yolanda.nohttp.download.DownloadRequest;
import com.yolanda.nohttp.download.Downloader;

import rx.Observable;
import rx.Subscriber;

public class RxDownLoad<T> {

	/**
	 * 同步下载数据
	 * @param what 下载的标记
	 * @param url  下载链接
	 * @param floadName 储存文件夹
	 * @param fileName  文件名称
	 * @param posADList 下载相应的数据
	 * @return
	 */
	public Observable<T> donwLoadSync(final int what, String url, String floadName,
									  String fileName, final PosADList posADList) {
		final Downloader d = new DownloadConnection();

		final DownloadRequest downloadRequest = NoHttp.createDownloadRequest(url,
				RequestMethod.GET, floadName, fileName, true, true);
		return Observable.create(new rx.Observable.OnSubscribe<T>() {

			@Override
			public void call(final Subscriber<? super T> t) {
				
				d.download(what, downloadRequest, new DownloadListener() {
					@Override
					public void onStart(int what, boolean isResume, long rangeSize,
										Headers responseHeaders, long allCount) {
						t.onStart();
					}

					@Override
					public void onProgress(int what, int progress, long fileCount) {
					}

					@Override
					public void onFinish(int what, String filePath) {
//						posADList.Memo = "已下载";
						// upFlow(PayContacts.UPDATA_URL, new File(filePath));
						// adUpState(PayContacts.UPDATA_URL, posADList);
//						t.onNext(t);
					}

					@Override
					public void onDownloadError(int what, Exception exception) {
						exception.printStackTrace();
					}

					@Override
					public void onCancel(int what) {
					}
				});
				
			}
		});
		
		
	}

}
