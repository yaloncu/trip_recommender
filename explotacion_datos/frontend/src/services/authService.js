import { signInWithPopup } from 'firebase/auth';
import { auth, googleProvider } from '@/firebaseConfig';

const signInWithGoogle = async () => {
  try {
    const signInWithGoogle = async () => {
      const result = await signInWithPopup(auth, googleProvider);
      const user = result.user;
      const idToken = await user.getIdToken();
      return { user, idToken };
    };
    const result = await signInWithPopup(auth, googleProvider);
    const user = result.user;
    const idToken = await user.getIdToken(); 

    console.log('Usuario autenticado:', user);
    return { user, idToken };
  } catch (error) {
    console.error('Error al iniciar sesión con Google:', error.message);
    throw error;
  }
};

export { signInWithGoogle };
