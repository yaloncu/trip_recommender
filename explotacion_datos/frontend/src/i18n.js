import { createI18n } from 'vue-i18n';
import en from './locales/en.json';
import fr from './locales/fr.json';
import es from './locales/es.json';
import de from './locales/de.json';

const messages = {
  en,
  fr,
  es,
  de
};

const i18n = createI18n({
  locale: 'en', 
  fallbackLocale: 'en', 
  messages
});

export default i18n;
