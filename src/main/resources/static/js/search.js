        const commands = [
      { icon: "🏠", label: "Home", action: () => navigateTo("home") },
      { icon: "🕵️", label: "Audit", action: () => navigateTo("audit")  },
      { icon: "⚙️", label: "Settings", action: () => navigateTo("home") },
      { icon: "🔒", label: "Change Password", action: () =>navigateTo("home")  },
      { icon: "👥", label: "Manage Users", action: () => navigateTo("home")  },
        { icon: "🎓", label: "Students", action: () => navigateTo("students")  }
    ];



    function openSearchCommandPalette() {
      document.getElementById("commandPalette").classList.add("show");
      document.getElementById("searchOverlay").classList.add("show");
      document.getElementById("commandInput").value = "";
      filterCommands();
      setTimeout(() => {
        document.getElementById("commandInput").focus();
      }, 100);
    }

    function closeSearchCommandPalette() {
      document.getElementById("commandPalette").classList.remove("show");
      document.getElementById("searchOverlay").classList.remove("show");
    }

    function filterCommands() {
      const keyword = document.getElementById("commandInput").value.toLowerCase();
      const list = document.getElementById("commandResults");
      list.innerHTML = "";

      const matched = commands.filter(cmd => cmd.label.toLowerCase().includes(keyword));
      if (matched.length === 0) {
        list.innerHTML = `<li style="text-align:center; color:#888;">No results found</li>`;
        return;
      }

      matched.forEach(cmd => {
        const li = document.createElement("li");
        li.innerHTML = `<span>${cmd.icon}</span> ${cmd.label}`;
        li.onclick = () => {
          closeSearchCommandPalette();
          cmd.action();
        };
        list.appendChild(li);
      });
    }

    // Bind Escape key to close
    document.addEventListener("keydown", function (e) {
      if (e.key === "Escape") closeSearchCommandPalette();
    });

    // Optional: Open when search clicked in header
    document.querySelector('.header-center input').addEventListener('click', openSearchCommandPalette);

