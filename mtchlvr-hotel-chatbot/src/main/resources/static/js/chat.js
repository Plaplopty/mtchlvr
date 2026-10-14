(() => {
 const toggle=document.getElementById('chat-toggle'), panel=document.getElementById('chat-panel');
 if(!toggle||!panel)return;
 const messages=document.getElementById('chat-messages'), form=document.getElementById('chat-form'), input=document.getElementById('chat-input');
 function addBubble(text,who){const el=document.createElement('div');el.className='chat-bubble '+who;el.textContent=text;messages.appendChild(el);messages.scrollTop=messages.scrollHeight;return el;}
 function openChat(){panel.hidden=false;toggle.setAttribute('aria-expanded','true');input.focus();}
 function closeChat(){panel.hidden=true;toggle.setAttribute('aria-expanded','false');toggle.focus();}
 toggle.addEventListener('click',()=>panel.hidden?openChat():closeChat());
 document.getElementById('chat-close').addEventListener('click',closeChat);
 messages.querySelectorAll('.chat-suggestions button').forEach(b=>b.addEventListener('click',()=>{input.value=b.textContent;form.requestSubmit();}));
 form.addEventListener('submit',async e=>{
  e.preventDefault();const text=input.value.trim();if(!text)return;
  addBubble(text,'user');input.value='';input.disabled=true;
  const pending=addBubble('One moment…','bot typing');
  try{const response=await fetch('/api/chat',{method:'POST',headers:{'Content-Type':'application/json','X-CSRF-TOKEN':getCsrfToken()},body:JSON.stringify({message:text})});
   if(response.status===401||response.status===403){pending.textContent='Please sign in with a customer account to use the hotel assistant.';}
   else if(!response.ok)throw new Error('Request failed');
   else{const data=await response.json();pending.textContent=data.reply||'Sorry, I could not process that. Please try again.';}
  }catch(err){pending.textContent='I couldn’t reach the hotel assistant. Please try again in a moment.';}
  finally{pending.classList.remove('typing');input.disabled=false;input.focus();messages.scrollTop=messages.scrollHeight;}
 });
 function getCsrfToken(){const meta=document.querySelector('meta[name="_csrf"]');return meta?meta.content:'';}
})();