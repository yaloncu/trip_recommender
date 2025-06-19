<template>
  <div class="side-menu">
    <!-- Grupos de viaje -->
    <ul class="menu-section">
      <li @click="$router.push('/groups')" class="menu-item">
        <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="lucide lucide-plane-icon lucide-plane"><path d="M17.8 19.2 16 11l3.5-3.5C21 6 21.5 4 21 3c-1-.5-3 0-4.5 1.5L13 8 4.8 6.2c-.5-.1-.9.1-1.1.5l-.3.5c-.2.5-.1 1 .3 1.3L9 12l-2 3H4l-1 1 3 2 2 3 1-1v-3l3-2 3.5 5.3c.3.4.8.5 1.3.3l.5-.2c.4-.3.6-.7.5-1.2z"/></svg>
        <span>{{ $t('menu.travelGroups') }}</span>
      </li>
      <li @click="$router.push('/groups/user')" class="menu-item">
        <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="lucide lucide-notepad-text-icon lucide-notepad-text"><path d="M8 2v4"/><path d="M12 2v4"/><path d="M16 2v4"/><rect width="16" height="18" x="4" y="4" rx="2"/><path d="M8 10h6"/><path d="M8 14h8"/><path d="M8 18h5"/></svg>
        <span>{{ $t('menu.myTrips') }}</span>
      </li>
      <li @click="$router.push('/groups/create')" class="menu-item">
        <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="lucide lucide-square-plus-icon lucide-square-plus"><rect width="18" height="18" x="3" y="3" rx="2"/><path d="M8 12h8"/><path d="M12 8v8"/></svg>
        <span>{{ $t('menu.createTrip') }}</span>
      </li>
    </ul>

    <hr class="separator" />

    <!-- Grupos de actividades -->
    <ul class="menu-section">
      <li @click="$router.push('/activities')" class="menu-item">
        <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="lucide lucide-landmark-icon lucide-landmark"><path d="M10 18v-7"/><path d="M11.12 2.198a2 2 0 0 1 1.76.006l7.866 3.847c.476.233.31.949-.22.949H3.474c-.53 0-.695-.716-.22-.949z"/><path d="M14 18v-7"/><path d="M18 18v-7"/><path d="M3 22h18"/><path d="M6 18v-7"/></svg>
        <span>{{ $t('menu.activities') }}</span>
      </li>
      <li @click="$router.push('/activities/user')" class="menu-item">
        <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="lucide lucide-notepad-text-icon lucide-notepad-text"><path d="M8 2v4"/><path d="M12 2v4"/><path d="M16 2v4"/><rect width="16" height="18" x="4" y="4" rx="2"/><path d="M8 10h6"/><path d="M8 14h8"/><path d="M8 18h5"/></svg>
        <span>{{ $t('menu.myActivities') }}</span>
      </li>
      <li @click="$router.push('/activities/create')" class="menu-item">
        <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="lucide lucide-square-plus-icon lucide-square-plus"><rect width="18" height="18" x="3" y="3" rx="2"/><path d="M8 12h8"/><path d="M12 8v8"/></svg>
        <span>{{ $t('menu.createActivity') }}</span>
      </li>
    </ul>

    <hr class="separator" />

    <!-- Perfil -->
    <ul class="menu-section">
      <li @click="$router.push('/profile')" class="menu-item">
        <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="lucide lucide-circle-user-round-icon lucide-circle-user-round"><path d="M18 20a6 6 0 0 0-12 0"/><circle cx="12" cy="10" r="4"/><circle cx="12" cy="12" r="10"/></svg>
        <span>{{ $t('menu.profile') }}</span>
      </li>

      <li @click="$router.push('/contact')" class="menu-item">
        <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="lucide lucide-mail-icon lucide-mail"><path d="m22 7-8.991 5.727a2 2 0 0 1-2.009 0L2 7"/><rect x="2" y="4" width="20" height="16" rx="2"/></svg>
        <span>{{ $t('menu.contact') }}</span>
      </li>
    </ul>
  </div>
</template>

<script>
export default {
  name: 'SideMenu',
};
</script>

<style scoped>
.side-menu {
  width: 220px;
  background-color: #2c3e50;
  color: white;
  padding: 20px 10px;
  border-right: 3px solid #1abc9c;
  min-height: 100vh;
  font-family: 'Poppins', sans-serif;
}

.menu-section {
  list-style: none;
  padding: 0;
  margin: 0 0 20px 0;
}

.menu-item {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 10px 12px;
  border-radius: 8px;
  cursor: pointer;
  transition: background-color 0.3s ease, transform 0.2s;
}

.menu-item:hover {
  background-color: #1abc9c;
  color: white;
  transform: translateX(5px);
}

.separator {
  border: none;
  height: 1px;
  background-color: #1abc9c;
  margin: 15px 0;
}

.icon {
  width: 18px;
  height: 18px;
  fill: white;
}
</style>
