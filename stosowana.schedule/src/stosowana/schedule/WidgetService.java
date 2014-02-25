package stosowana.schedule;

import android.annotation.TargetApi;
import android.content.Intent;
import android.widget.RemoteViewsService;

@TargetApi(11)
public class WidgetService extends RemoteViewsService {
 
	@Override
	public RemoteViewsFactory onGetViewFactory(Intent intent) {
		
		return new RemoteViewsProvider(this.getApplicationContext(), intent);
	}
 
}