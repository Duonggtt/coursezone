$(document).ready(function () {
    // Xác định URL hiện tại
    var currentUrl = window.location.href;

    // Duyệt qua tất cả các liên kết trong navbar
    $('.nav-item a').each(function () {
        // Nếu href của liên kết này khớp với URL hiện tại
        if (this.href === currentUrl) {
            // Thêm class 'active' vào liên kết này
            $(this).addClass('active');
        }
    });

    $(window).scroll(function() {
        if ($(this).scrollTop() > 50) {
            $('.navbar').removeClass('navbar-transparent').addClass('navbar-solid');
        } else {
            $('.navbar').removeClass('navbar-solid').addClass('navbar-transparent');
        }
    });

    // Khởi tạo trạng thái ban đầu
    if ($(window).scrollTop() > 50) {
        $('.navbar').addClass('navbar-solid');
    } else {
        $('.navbar').addClass('navbar-transparent');
    }
});

//chuyen dong của content khi scroll
function checkScroll() {
    var elements = document.querySelectorAll('.animate');
    var windowHeight = window.innerHeight;

    elements.forEach(function(element) {
        var elementPosition = element.getBoundingClientRect().top;
        if (elementPosition - windowHeight <= 0) {
            element.classList.add('animate-on-scroll');
        } else {
            element.classList.remove('animate-on-scroll');
        }
    });
}

// Thực hiện hàm checkScroll ngay khi trang tải
checkScroll();

function checkScrollX() {
    var elements = document.querySelectorAll('.animate-x');
    var windowHeight = window.innerHeight;

    elements.forEach(function(element) {
        var elementPosition = element.getBoundingClientRect().top;
        if (elementPosition - windowHeight <= 0) {
            element.classList.add('animate-on-scroll');
        } else {
            element.classList.remove('animate-on-scroll');
        }
    });
}

// Thực hiện hàm checkScroll ngay khi trang tải
checkScrollX();

// Thực hiện hàm checkScroll mỗi khi người dùng cuộn trang
window.addEventListener('scroll', checkScroll);


document.addEventListener('DOMContentLoaded', function() {
    const testimonialImages = document.querySelectorAll('.testimonial-images img');

    testimonialImages.forEach(img => {
        img.addEventListener('click', function() {
            // Xóa class active từ tất cả các hình ảnh
            testimonialImages.forEach(image => image.classList.remove('active'));

            // Thêm class active vào hình ảnh được click
            this.classList.add('active');

            // Lấy index của hình ảnh được click
            const index = parseInt(this.getAttribute('data-index'));

            // Di chuyển hình ảnh được click vào giữa
            const container = this.parentElement;
            const scrollAmount = img.offsetWidth * (index - 2);
            container.scrollTo({
                left: scrollAmount,
                behavior: 'smooth'
            });
        });
    });
});


document.addEventListener('DOMContentLoaded', function() {
    new Swiper('.swiper-container', {
        slidesPerView: 'auto',
        spaceBetween: 20,
        loop: true,
        pagination: {
            el: '.swiper-pagination',
            clickable: true,
        },
        navigation: {
            nextEl: '.swiper-button-next',
            prevEl: '.swiper-button-prev',
        },
        autoplay: {
            delay: 3000,
            disableOnInteraction: false,
        },
        breakpoints: {
            // Khi màn hình >= 768px
            768: {
                slidesPerView: 2,
            },
            // Khi màn hình >= 1024px
            1024: {
                slidesPerView: 3,
            }
        }
    });
});

document.addEventListener('DOMContentLoaded', function() {
    const gridBtn = document.getElementById('grid-view');
    const listBtn = document.getElementById('list-view');
    const courseContainer = document.getElementById('course-container');
    const courseItems = document.querySelectorAll('.course-item');

    function setGridView() {
        courseContainer.classList.remove('row-cols-md-12');
        courseContainer.classList.add('row-cols-md-3');
        courseItems.forEach(item => {
            item.classList.remove('mb-4');
            item.classList.add('mb-4');
        });
        gridBtn.classList.add('active');
        listBtn.classList.remove('active');
    }

    function setListView() {
        courseContainer.classList.remove('row-cols-md-3');
        courseContainer.classList.add('row-cols-md-12');
        courseItems.forEach(item => {
            item.classList.remove('mb-4');
            item.classList.add('mb-4');
        });
        listBtn.classList.add('active');
        gridBtn.classList.remove('active');
    }

    gridBtn.addEventListener('click', setGridView);
    listBtn.addEventListener('click', setListView);

    // Set default view
    setGridView();
});