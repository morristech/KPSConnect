bash buildReleaseApp.sh
if [ $? -eq 0 ];then
export tg_message="\[KPSConnect] Build number $BUILD_NUMBER succeeded, changes [here](http://jenkins.msfjarvis.me/job/afh-browser-master/$BUILD_NUMBER/changes). Build artifact [here](http://jenkins.msfjarvis.me/job/afh-browser-master/lastSuccessfulBuild/artifact/app/app-nightly-release.apk)"
curl "https://api.telegram.org/bot$TG_BOT_ID/sendmessage" --data "text=$tg_message&chat_id=$TG_GRP_ID&parse_mode=Markdown" 2>&1 >/dev/null
curl "https://api.telegram.org/bot$TG_BOT_ID/sendmessage" --data "text=$tg_message&chat_id=$TG_GRP_ID_2&parse_mode=Markdown" 2>&1 >/dev/null
else
export tg_message="\[KPSConnect] Build number $BUILD_NUMBER failed, changes [here](http://jenkins.msfjarvis.me/job/afh-browser-master/$BUILD_NUMBER/changes). Console output [here](http://jenkins.msfjarvis.me/job/afh-browser-master/lastBuild/console)"
curl "https://api.telegram.org/bot$TG_BOT_ID/sendmessage" --data "text=$tg_message&chat_id=$TG_GRP_ID&parse_mode=Markdown" 2>&1 >/dev/null
curl "https://api.telegram.org/bot$TG_BOT_ID/sendmessage" --data "text=$tg_message&chat_id=$TG_GRP_ID_2&parse_mode=Markdown" 2>&1 >/dev/null
fi
