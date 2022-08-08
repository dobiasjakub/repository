using AutoMapper;
using EucyonBookIt.Database;
using EucyonBookIt.Models;
using EucyonBookIt.Models.DTOs;
using EucyonBookIt.Models.Exceptions;
using EucyonBookIt.Models.MailModels;
using EucyonBookIt.Resources;
using EucyonBookIt.Services.Interfaces;
using Microsoft.EntityFrameworkCore;
using Microsoft.Extensions.Localization;
using System.Security.Cryptography;
using System.Text.RegularExpressions;

namespace EucyonBookIt.Services
{
    public class UserService : IUserService
    {
        private readonly ApplicationContext _context;
        private readonly IMapper _mapper;
        private readonly IEmailService _email;
        private readonly IStringLocalizer<StringResource> _localizer;

        public UserService(ApplicationContext context, IMapper mapper, IEmailService email, IStringLocalizer<StringResource> localizer)
        {
            _context = context;
            _mapper = mapper;
            _email = email;
            _localizer = localizer;
        }

        public bool ValidateEmailAddress(string emailAddress, out string returnMessage)
        {
            //min length can be u@d but I'll enforce TLD part (eg. 6 chars)
            int minTotalLength = 6;
            int maxTotalLength = 60;

            if (emailAddress.Length > maxTotalLength || emailAddress.Length < minTotalLength)
            {
                returnMessage = _localizer["EmailAddressInvalidLength"];
                return false;
            }

            var emailPattern = @"^[\w+\.%-]{1,64}@(?:[\w+%-]+\.)+[aA-zZ]{2,}$";

            if (!Regex.IsMatch(emailAddress, emailPattern))
            {
                returnMessage = _localizer["EmailAddressInvalidFormat"];
                return false;
            }

            returnMessage = "Validation OK";
            return true;
        }

        public bool ValidatePassword(string password, string passwordRetype, out string returnMessage)
        {
            if (password != passwordRetype)
            {
                returnMessage = _localizer["PasswordValidationNotMatch"];
                return false;
            }

            int minLength = 6;
            int maxLength = 30;

            if (password.Length < minLength || password.Length > maxLength)
            {
                returnMessage = _localizer["PasswordValidationInvalidLength"];
                return false;
            }

            var pswPattern = @"(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?!.*[^aA-zZ0-9]).{6,}";

            if (!Regex.IsMatch(password, pswPattern))
            {
                returnMessage = _localizer["PasswordValidationInvalidFormat"];
                return false;
            }

            returnMessage = "Validation OK";
            return true;
        }

        public User? RegisterUser(UserRegistrationDTO dto)
        {
            if (!ValidateEmailAddress(dto.EmailAddress, out string returnMessage))
                throw new InputValidationException(returnMessage);
            else if (!ValidatePassword(dto.Password, dto.PasswordRetype, out returnMessage))
                throw new InputValidationException(returnMessage);       
            else if (!new string[] { _localizer["RoleCustomer"], _localizer["RoleManager"] }.Contains(dto.Role.ToLower()))
                throw new InputValidationException(_localizer["RegistrationInvalidRole"]);

            var newUser = _mapper.Map<User>(dto);

            newUser.RoleId = dto.Role.ToLower().Equals(_localizer["RoleCustomer"]) ? 1 : 2;

            newUser = AddUser(newUser, true);

            return newUser;
        }

        public User AddUser(User newUser, bool activate)
        {
            if (GetUserBy(newUser.EmailAddress) != null)
                throw new InputValidationException(_localizer["UserEmailAddressTaken"]);

            newUser.IsActive = activate;

            var user = _context.Users.Add(newUser).Entity;
            _context.SaveChanges();

            return user;
        }

        public User? GetUserBy(string emailAddress, bool includePerson = false)
        {
            if (includePerson)
                return _context.Users.Include(u => u.Person).FirstOrDefault(u => u.EmailAddress.ToLower() == emailAddress.ToLower());

            return _context.Users.FirstOrDefault(u => u.EmailAddress.ToLower() == emailAddress.ToLower());
        }

        public User? GetUserBy(string emailAddress, string password)
        {
            return _context.Users.Include(u => u.Role).FirstOrDefault(u => u.EmailAddress.ToLower() == emailAddress.ToLower() && u.Password == password);
        }

        public User? LoginUser(UserLoginDTO dto)
        {
            return GetUserBy(dto.EmailAddress, dto.Password);
        }

        public User? GetUserBy(User? user)
        {
            return user == null ? null : _context.Users.Where(u => u.UserId == user.UserId)
                                                       .Include(p => p.Person)
                                                       .Include(r => r.Role)
                                                       .FirstOrDefault();
        }

        public bool UpdateUser(User? user)
        {
            if (user == null || user.UserId < 1)
            {
                return false;
            }

            User updUser = _context.Users.Update(user).Entity;
            _context.SaveChanges();

            return updUser.UserId > 0;
        }

        public List<User> GetUsers()
        {
            return _context.Users.Include(p => p.Person).Include(r => r.Role).ToList();
        }

        public User? ResetPassword(ResetPasswordDTO dto)
        {
            var user = GetUserBy(dto.EmailAddress, includePerson: true);
            if (user == null)
                return null;

            var newPassword = GenerateNewPassword();

            var mail = new MimePasswordReset(user.Person.FirstName + " " + user.Person.LastName, user.EmailAddress, newPassword);
            var response = _email.Send(mail);
            if (!response)
                return null;

            user.Password = newPassword;
            user = _context.Update(user).Entity;
            _context.SaveChanges();

            return user;
        }

        public string GenerateNewPassword() 
        {
            int pswLength = 24;
            var allowedChars = new string[] {
                "abcdefghijklmnopqrstuvwxyz",
                "ABCDEFGHIJKLMNOPQRSTUVWXYZ",
                "0123456789"
            };

            var pswList = new List<char>();

            //guarantees new psw will include at least one char from every group, thus adhering to psw requirements
            for (int i = 0; i < allowedChars.Length; i++)
            {
                int listIndex = i > 0 ? RandomNumberGenerator.GetInt32(0, pswList.Count) : i;
                pswList.Insert(listIndex, allowedChars[i][RandomNumberGenerator.GetInt32(0, allowedChars[i].Length)]);
            }

            var allowedConcat = String.Join("", allowedChars);
            
            for (int i = 0; i < pswLength - allowedChars.Length; i++)
            {
                pswList.Insert(RandomNumberGenerator.GetInt32(0, pswList.Count), allowedConcat[RandomNumberGenerator.GetInt32(0, allowedConcat.Length)]);
            }
            
            return String.Join("", pswList);
        }
    }
}
