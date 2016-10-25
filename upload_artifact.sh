TIMESTAMP="$(date --utc +%s)"
# length and contents are not important, "abcdef" would work as well
TOKEN="$(cat /dev/urandom | tr -d -c '[:alnum:]' | head -c $(( 32 - ${#TIMESTAMP} )))"

SIGNATURE="$(printf "${TIMESTAMP}${TOKEN}" \
             | openssl dgst -sha256 -hmac "${SECRET}" -binary \
             | openssl enc -base64)"

[ -f app/app-release.apk] && mv app/app-release.apk app/kpsconnect-beta-$(git -C ${pwd} rev-parse --short=7 HEAD).apk || exit 1
# order does not matter; any skipped fields in Authorization will be set to defaults
curl -T \
	--header "Timestamp: ${TIMESTAMP}" \
	--header "Token: ${TOKEN}" \
	--header "Authorization: Signature keyId='${KEYID}',signature='${SIGNATURE}'" \
	"app/kpsconnect-beta-$(git -C ${pwd} rev-parse --short=7 HEAD).apk" "https://kpsconnect.msfjarvis.me/ci-builds"
