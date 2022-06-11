import Vue from 'vue'
import VueI18n from 'vue-i18n'

Vue.use(VueI18n)

function loadLocaleMessages () {
  const locales = require.context('@/assets/locales', true, /[A-Za-z0-9-_,\s]+\.json$/i)
  const messages = {}
  locales.keys().forEach(key => {
    const matched = key.match(/([A-Za-z0-9-_]+)\./i)
    if (matched && matched.length > 1) {
      const locale = matched[1]
      messages[locale] = locales(key)
    }
  })
  return messages
}

export default new VueI18n({
  locale: defaultLocale(),
  messages: loadLocaleMessages()
})

function defaultLocale() {
  // Login 시점에 선택한 언어를 브라우저에 저장해 두고 계속 사용 (바꾸고 싶으면 다시 로그인하면 됨)
  let locale = localStorage.getItem('langCd');
  if (!locale) {
    locale = 'en';
  }
  return locale;
}