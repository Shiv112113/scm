console.log("contacts.js");

const baseURL = "http://localhost:8081";

// set the modal menu element
const viewContactModal = document.getElementById('default_contact_view_modal');

// options with default values
const options = {
    placement: 'bottom-right',
    backdrop: 'dynamic',
    backdropClasses:
        'bg-gray-900/50 dark:bg-gray-900/80 fixed inset-0 z-40',
    closable: true,
    onHide: () => {
        console.log('modal is hidden');
    },
    onShow: () => {
        console.log('modal is shown');
    },
    onToggle: () => {
        console.log('modal has been toggled');
    },
};

// instance options object
const instanceOptions = {
  id: 'default_contact_view_modal',
  override: true
};

const contactModal = new Modal(viewContactModal, options, instanceOptions);

function openContactModal() {
    contactModal.show();
}

function hideContactModal() {
    contactModal.hide();
}

async function loadContactData(id) {
    console.log(id);

    try {
        const data = await (
            await fetch(`${baseURL}/api/contacts/${id}`)
        ).json();

        console.log(data);

        document.querySelector('.contact_name').innerHTML = data.name;
        document.querySelector('#contact_image').src = data.contactPic;
        document.querySelector('#contact_name').innerHTML = data.name;
        document.querySelector('#contact_email').innerHTML = data.email;
        document.querySelector('#contact_phone_number').innerHTML = data.phoneNumber;
        document.querySelector('#contact_address').innerHTML = data.address;
        if(data.favourite == true) {
            document.querySelector('#contact_favourite').innerHTML = "<svg class='-mx-6 w-6 h-6 text-yellow-400 dark:text-yellow-300 me-1' aria-hidden='true' xmlns='http://www.w3.org/2000/svg' fill='currentColor' viewBox='0 0 22 20'><path d='M20.924 7.625a1.523 1.523 0 0 0-1.238-1.044l-5.051-.734-2.259-4.577a1.534 1.534 0 0 0-2.752 0L7.365 5.847l-5.051.734A1.535 1.535 0 0 0 1.463 9.2l3.656 3.563-.863 5.031a1.532 1.532 0 0 0 2.226 1.616L11 17.033l4.518 2.375a1.534 1.534 0 0 0 2.226-1.617l-.863-5.03L20.537 9.2a1.523 1.523 0 0 0 .387-1.575Z'/></svg>";
        }else {
            document.querySelector('#contact_favourite').innerHTML = "";
        }

        document.querySelector('#contact_social_link_1').innerHTML = data.socialLink1;
        document.querySelector('#contact_social_link_1').href = data.socialLink1;

        document.querySelector('#contact_social_link_2').innerHTML = data.socialLink2;
        document.querySelector('#contact_social_link_2').href = data.socialLink2;

        openContactModal();

    } catch (error) {
        console.log("Error : ", error);
    }
}

// Delete Contact... 
async function deleteContact(id) {
    try {
        const data = await (
            await fetch(`${baseURL}/api/contacts/${id}`)
        ).json();

        Swal.fire({
            title: "Are you sure?",
            icon: "warning",
            html: `Delete Contact <b>${data.name}</b>`,
            showCancelButton: true,
            confirmButtonColor: "#3085d6",
            cancelButtonColor: "#d33",
            confirmButtonText: "Yes, delete it!"
          }).then((result) => {
            if (result.isConfirmed) {
                const url = `${baseURL}/user/contacts/delete/${id}`;
                window.location.replace(url);
            }
          });

    } catch (error) {
        console.log("Error : ", error);
    }
}

// Export Contacts to Excel...
function exportData() {
    TableToExcel.convert(document.getElementById("contacts-table"), {
        name: "contacts.xlsx",
        sheet: {
            name: "Sheet 1"
        }
    });
}