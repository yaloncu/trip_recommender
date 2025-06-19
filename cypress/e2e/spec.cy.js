describe('template spec', () => {
  it('passes', () => {
    cy.visit('http://localhost:8080');
    cy.contains('Welcome');
  });
  it('debe registrar a un nuevo usuario correctamente', () => {
    cy.visit('http://localhost:8080');
    cy.get('button.signup-button').click();
    cy.url().should('include', '/signup');
    cy.get('#name').type('Test User');
    cy.get('#email').type(`testuser+${Date.now()}@example.com`); // Email único
    cy.get('#password').type('TestPassword123');
    cy.get('form').submit();
    cy.contains('Join a group');
  });
  it('muestra el grupo Couple trip en la lista', () => {
    cy.visit('http://localhost:8080/groups'); // Ajusta si tu ruta es diferente
    cy.wait(1000);
    cy.contains('.group-title', 'Couples trip').should('exist');
  });
  it('debe inscribirse a un grupo correctamente', () => {
    cy.visit('http://localhost:8080');
    cy.get('button.signup-button').click();
    cy.url().should('include', '/signup');
    cy.get('#name').type('Test User');
    cy.get('#email').type(`testuser+${Date.now()}@example.com`); // Email único
    cy.get('#password').type('TestPassword123');
    cy.get('form').submit();
    cy.contains('.group-title', 'Couples trip')
      .should('exist')
      .parent()
      .find('.join-button')
      .click();
    cy.get('.group-details-card', { timeout: 5000 }).should('exist');
    cy.get('.radio-label').contains('Romantic').click();
    cy.get('.join-button').click();
    cy.contains('.already-member-message', 'You are already a member of this group').should('exist');
  });
  it('Rellena el formulario y crea una nueva actividad', () => {
    cy.visit('http://localhost:8080');
    cy.get('button.signup-button').click();
    cy.url().should('include', '/signup');
    cy.get('#name').type('Test User');
    cy.get('#email').type(`testuser+${Date.now()}@example.com`);
    cy.get('#password').type('TestPassword123');
    cy.get('form').submit();
    cy.contains('.menu-item', 'Create Activity').click();    
    cy.get('input[placeholder="Activity Title"]').type('Excursión a la montaña');

    // Descripción
    cy.get('textarea[placeholder="Description:"]').type('Una caminata guiada para explorar rutas de montaña');

    // Ubicación
    cy.get('input[placeholder="Location"]').type('Madrid');

    // Selecciona el tipo de actividad (ej. Aventura)
    cy.contains('.radio-box', 'Adventure').click();

    // Fecha y hora de inicio (formato compatible con datetime-local)
    const now = new Date();
    const formatted = now.toISOString().slice(0, 16); // yyyy-MM-ddTHH:mm
    cy.get('input[type="datetime-local"]').type(formatted);

    // Crear la actividad
    cy.get('.create-group-button').click();

    // Verifica algún resultado esperado (puedes ajustar según implementación)
    cy.contains('Public Activities').should('exist'); // Solo si muestra este mensaje
  });
})