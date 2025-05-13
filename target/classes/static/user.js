document.addEventListener('DOMContentLoaded', () => {
  const form = document.getElementById('moderationForm');
  const userIdInput = document.getElementById('userId');
  const contentInput = document.getElementById('content');
  const charCount = document.getElementById('charCount');
  const resultBox = document.getElementById('result');
  const btnText = document.querySelector('.btn-text');
  const btnLoader = document.querySelector('.btn-loader');

  // Character count
  contentInput.addEventListener('input', () => {
    charCount.textContent = contentInput.value.length;
  });

  // Form submission
  form.addEventListener('submit', async (e) => {
    e.preventDefault();

    // UI state: loading
    btnText.classList.add('hidden');
    btnLoader.classList.remove('hidden');
    resultBox.classList.add('hidden');
    resultBox.innerHTML = '';

    const userId = userIdInput.value.trim();
    const content = contentInput.value.trim();

    try {
      const response = await fetch('/api/moderate', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ userId, content })
      });

      const result = await response.json();

      resultBox.classList.remove('hidden');
      resultBox.innerHTML = `
        <p><strong>Flagged:</strong> ${result.flagged ? 'Yes' : 'No'}</p>
        <p><strong>Message:</strong> ${result.message}</p>
        <p><strong>Punishment:</strong> ${result.punishment}</p>
      `;
    } catch (err) {
      resultBox.classList.remove('hidden');
      resultBox.innerHTML = `<p class="error">Error: Could not process moderation.</p>`;
      console.error(err);
    } finally {
      // Reset UI state
      btnText.classList.remove('hidden');
      btnLoader.classList.add('hidden');
    }
  });
});
