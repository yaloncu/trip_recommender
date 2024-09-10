import { initializeApp } from 'firebase/app';
import { getAuth, GoogleAuthProvider, signInWithPopup } from 'firebase/auth';

const firebaseConfig = {
  apiKey: "AIzaSyBBnKZM4SQ8mZ_xWv9s1Qwq_ja6_jwDQ4M",
  authDomain: "trip-726fc.firebaseapp.com",
  projectId: "trip-726fc",
  storageBucket: "trip-726fc.appspot.com",
  messagingSenderId: "871596666349",
  appId: "1:871596666349:web:96b1d2ce354696a2ee4be4",
  measurementId: "G-Y54EE5L9V7"
};

// Inicializar Firebase
const app = initializeApp(firebaseConfig);
export const auth = getAuth(app);
export const googleProvider = new GoogleAuthProvider();
